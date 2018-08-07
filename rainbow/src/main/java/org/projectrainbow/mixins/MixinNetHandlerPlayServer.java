package org.projectrainbow.mixins;

import PluginReference.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.projectrainbow.*;
import org.projectrainbow.commands._CmdIgnore;
import org.projectrainbow.interfaces.IMixinCPacketCustomPlayload;
import org.projectrainbow.interfaces.IMixinEntityPlayerMP;
import org.projectrainbow.interfaces.IMixinOutboundPacketSoundEffect;
import org.projectrainbow.interfaces.IMixinOutboundPacketSpawnPosition;
import org.projectrainbow.launch.Bootstrap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(NetHandlerPlayServer.class)
public class MixinNetHandlerPlayServer {

    @Shadow
    public EntityPlayerMP player;
    @Shadow
    private int teleportId;
    @Shadow
    private Vec3d targetPos;

    @Shadow
    @Final
    private IntHashMap<Short> pendingTransactions;
    @Shadow
    private int lastPositionUpdate;
    @Shadow
    private int networkTickCount;

    @Redirect(method = "processPlayer", at = @At(value = "INVOKE", target = "org/apache/logging/log4j/Logger.warn(Ljava/lang/String;Ljava/lang/Object;)V ", remap = false))
    private void doLogWarning(Logger logger, String message, Object arg) {
        if (!message.contains("is sending move packets too frequently") || !_DiwUtils.DoHideAnnoyingDefaultServerOutput) {
            logger.warn(message, arg);
        }
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void onDisconnect(ITextComponent var1, CallbackInfo callback) {
        _JOT_OnlineTimeUtils.HandlePlayerLogout(player.getGameProfile().getName(), player.getUniqueID());
        Hooks.onPlayerLogout(player.getGameProfile().getName(), player.getUniqueID());
    }

    @Inject(method = "processPlayerDigging", at = @At(value = "INVOKE", target = "net.minecraft.entity.player.EntityPlayerMP.markPlayerActive()V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onPacketPlayerDigging(CPacketPlayerDigging packet, CallbackInfo callback, WorldServer worldServer, BlockPos blockPos) {
        if (packet.getAction().equals(CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptBlockBreak((MC_Player) player, new MC_Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), player.dimension), ei);
            if (ei.isCancelled) {
                player.connection.sendPacket(new SPacketBlockChange(worldServer, blockPos));
                callback.cancel();
            }
        }
    }

    @Inject(method = "processTryUseItemOnBlock", at = @At(value = "INVOKE", target = "net.minecraft.entity.player.EntityPlayerMP.markPlayerActive()V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void processRightClickBlock(CPacketPlayerTryUseItemOnBlock packet, CallbackInfo callback, WorldServer worldServer,
                                        EnumHand hand, ItemStack itemStack, BlockPos blockPos,
                                        EnumFacing clickedFace) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptPlaceOrInteract((MC_Player) player, new MC_Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), player.dimension), PluginHelper.directionMap.get(clickedFace), PluginHelper.handMap.get(hand), ei);
        if (ei.isCancelled) {
            for (BlockPos pos : BlockPos.getAllInBox(blockPos.add(-1, -1, -1), blockPos.add(1, 1, 1))) {
                if (pos.getY() < 0 || pos.getY() > 255) continue;
                this.player.connection.sendPacket(new SPacketBlockChange(worldServer, pos));
            }
            callback.cancel();
        } else {
            _DynReward.HandleInteract((MC_Player) player, blockPos.getX(), blockPos.getY(), blockPos.getZ(), player.dimension);
        }
    }

    @Inject(method = "processTryUseItem", at = @At(value = "INVOKE", target = "net.minecraft.entity.player.EntityPlayerMP.markPlayerActive()V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void onRightClickAir(CPacketPlayerTryUseItem packet, CallbackInfo callbackInfo, WorldServer world, EnumHand hand, ItemStack itemStack) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptItemUse((MC_Player) player, (MC_ItemStack) (Object) itemStack, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "processChatMessage", at = @At(value = "INVOKE", target = "net.minecraft.entity.player.EntityPlayerMP.markPlayerActive()V"), cancellable = true)
    public void onChat(CPacketChatMessage packet, CallbackInfo callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onPlayerInput((MC_Player) player, packet.getMessage(), ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        } else {
            if (!((MC_Player) player).isOp()) {
                String var11 = _JoeCommandStats.HandleNewCommand(((MC_Player) player).getName(), packet.getMessage());

                if (var11 != null) {
                    ((MC_Player) player).kick(var11);
                    callbackInfo.cancel();
                    return;
                }

                if (_DiwUtils.HasBadLanguage(packet.getMessage())) {
                    _DiwUtils.NotifyCensor(((MC_Player) player).getName(), packet.getMessage());
                    long var14 = _DiwUtils.IncreaseEventCount("CENSOR." + packet.getMessage());
                    String swearMsg = String.format(
                            "Kicking for Language (Censor #%d): %s: %s",
                            var14, packet.getMessage(), ((MC_Player) player).getName());

                    System.out.println(
                            "--------------------------------------------");
                    System.out.println(swearMsg);
                    System.out.println(
                            "--------------------------------------------");
                    ((MC_Player) player).kick(
                            "Censor #" + var14
                                    + ": Watch language, Children play");
                    callbackInfo.cancel();
                    return;
                }
            }

            if (packet.getMessage().startsWith("/")) {
                if (_EmoteUtils.HandleEmote((MC_Player) player, packet.getMessage().substring(1))) {
                    callbackInfo.cancel();
                    return;
                }
            }
        }
    }

    @ModifyVariable(method = "processChatMessage", at = @At(value = "INVOKE", target = "Ljava/lang/String;startsWith(Ljava/lang/String;)Z", remap = false))
    private String chatColor(String message) {
        if (message.startsWith("/") && !message.startsWith("/w") && !message.startsWith("/whisper") && !message.startsWith("/msg"))
            return message;
        if (((MC_Player) player).hasPermission("rainbow.chatcolor")) {
            return _DiwUtils.TranslateChatString(message, ((MC_Player) player).isOp());
        }
        return message;
    }

    private static final Pattern patternLink = Pattern.compile("(?<=(\\W|^))(?:(https?)://)?([-\\w_\\.]{2,}\\.[a-z]{2,8})(/\\S*)?(?=(\\W|$))", Pattern.CASE_INSENSITIVE);

    @Redirect(method = "processChatMessage", at = @At(value = "INVOKE", target = "net.minecraft.server.management.PlayerList.sendMessage(Lnet/minecraft/util/text/ITextComponent;Z)V"))
    private void onChatSent(PlayerList playerList, ITextComponent message, boolean b) {
        TextComponentTranslation t = (TextComponentTranslation) message;
        ITextComponent player = (ITextComponent) t.getFormatArgs()[0];
        String msg = (String) t.getFormatArgs()[1];

        TextComponentString result = new TextComponentString("");
        result.appendSibling(new TextComponentString("<"));
        result.appendSibling(player);
        result.appendSibling(new TextComponentString("> "));

        Matcher matcher = patternLink.matcher(msg);
        while (matcher.find()) {
            StringBuffer buffer = new StringBuffer();
            matcher.appendReplacement(buffer, "");
            result.appendSibling(new TextComponentString(buffer.toString()));

            String link = matcher.group();
            if (!link.startsWith("http://") && !link.startsWith("https://")) {
                link = "http://" + link;
            }
            result.appendSibling(new TextComponentString(matcher.group()).setStyle(new Style().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link))));
        }

        StringBuffer buffer = new StringBuffer();
        matcher.appendTail(buffer);
        result.appendSibling(new TextComponentString(buffer.toString()));

        _DiwUtils.getMinecraftServer().sendMessage(result);

        SPacketChat packet = new SPacketChat(result, ChatType.byId((byte) (b ? 1 : 0)));
        for (EntityPlayerMP receiver : playerList.getPlayers()) {
            if (!_CmdIgnore.IsIgnoring(receiver.getGameProfile().getName(), this.player.getGameProfile().getName())) {
                receiver.connection.sendPacket(packet);
            }
        }

    }

    @Inject(method = "handleSlashCommand", at = @At("HEAD"))
    private void onCommand(String command, CallbackInfo callbackInfo) {
        if (!command.startsWith("/login")) {
            Bootstrap.logger.info("" + player.getGameProfile().getName() + ": " + command);
        }
    }

    @Inject(method = "setPlayerLocation(DDDFFLjava/util/Set;)V", at = @At("HEAD"), cancellable = true)
    private void hookTeleport(double x, double y, double z, float yaw, float pitch, Set<SPacketPlayerPosLook.EnumFlags> flags, CallbackInfo callbackInfo) {
        double dx = flags.contains(SPacketPlayerPosLook.EnumFlags.X) ? x : player.posX - x;
        double dy = flags.contains(SPacketPlayerPosLook.EnumFlags.Y) ? y : player.posY - y;
        double dz = flags.contains(SPacketPlayerPosLook.EnumFlags.Z) ? z : player.posZ - z;
        float finalYaw = flags.contains(SPacketPlayerPosLook.EnumFlags.Y_ROT) ? yaw + player.rotationYaw : yaw;
        float finalPitch = flags.contains(SPacketPlayerPosLook.EnumFlags.X_ROT) ? pitch + player.rotationPitch : pitch;
        // teleport
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptPlayerTeleport((MC_Player) player, new MC_Location(player.posX + dx, player.posY + dy, player.posZ + dz, player.dimension, finalYaw, finalPitch), ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();

            this.targetPos = new Vec3d(player.posX, player.posY, player.posZ);
            if (++this.teleportId == 2147483647) {
                this.teleportId = 0;
            }
            this.lastPositionUpdate = this.networkTickCount;

            player.connection.sendPacket(new SPacketPlayerPosLook(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch, Collections.emptySet(), this.teleportId));
        }
    }

    @Inject(method = "processPlayer", at = @At(value = "FIELD", target = "net.minecraft.network.NetHandlerPlayServer.server:Lnet/minecraft/server/MinecraftServer;", ordinal = 0), cancellable = true)
    private void onPlayerMove(CPacketPlayer packet, CallbackInfo callbackInfo) {
        double x = packet.getX(this.player.posX);
        double y = packet.getY(this.player.posY);
        double z = packet.getZ(this.player.posZ);
        float yaw = packet.getYaw(this.player.rotationYaw);
        float pitch = packet.getPitch(this.player.rotationPitch);
        double oldX = this.player.posX;
        double oldY = this.player.posY;
        double oldZ = this.player.posZ;
        float oldYaw = this.player.rotationYaw;
        float oldPitch = this.player.rotationPitch;

        if (Math.abs(x - oldX) > 1E-4 || Math.abs(y - oldY) > 1E-4 || Math.abs(z - oldZ) > 1E-4 || Math.abs(yaw - oldYaw) > 1E-4 || Math.abs(pitch - oldPitch) > 1E-4) {

            MC_EventInfo ei = new MC_EventInfo();
            MC_Player player = (MC_Player) this.player;

            MC_Location from = player.getLocation();
            MC_Location to = new MC_Location(x, y, z, from.dimension, yaw, pitch);

            Hooks.onAttemptPlayerMove(player, from, to, ei);

            if (ei.isCancelled) {
                this.targetPos = new Vec3d(oldX, oldY, oldZ);
                this.player.connection.sendPacket(new SPacketPlayerPosLook(oldX, oldY, oldZ, oldYaw, oldPitch, Collections.emptySet(), ++this.teleportId));
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    private void onPacketSent(final Packet<?> argPacket, CallbackInfo callbackInfo) {
        if (argPacket instanceof SPacketSpawnPosition) {
            BlockPos pos = ((IMixinOutboundPacketSpawnPosition) argPacket).getPos();

            ((IMixinEntityPlayerMP) player).onCompassTargetUpdated(new MC_Location(
                    (double) pos.getX(), (double) pos.getY(),
                    (double) pos.getZ(), this.player.dimension));
        }

        if (argPacket instanceof IMixinOutboundPacketSoundEffect) {
            IMixinOutboundPacketSoundEffect packet = (IMixinOutboundPacketSoundEffect) argPacket;
            MC_EventInfo ei = new MC_EventInfo();
            MC_Location loc = new MC_Location(
                    (double) (packet.getX() / 8), (double) (packet.getY() / 8),
                    (double) (packet.getZ() / 8), this.player.dimension);


            Hooks.onPacketSoundEffect((MC_Player) player, packet.getSoundName(), loc, ei);

            if (ei.isCancelled) {
                callbackInfo.cancel();
            }
        }
    }

    @Redirect(method = "processUpdateSign", at = @At(value = "INVOKE", target = "net.minecraft.util.text.TextFormatting.getTextWithoutFormattingCodes(Ljava/lang/String;)Ljava/lang/String;"))
    private String stripColorCodes(String s) {
        if (((MC_Player) player).hasPermission("rainbow.signcolor")) {
            return s;
        } else {
            return ChatColor.StripColor(s);
        }
    }

    @Inject(method = "processUpdateSign", at = @At(value = "INVOKE", target = "net.minecraft.network.play.client.CPacketUpdateSign.getLines()[Ljava/lang/String;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void onChangingSign(CPacketUpdateSign packet, CallbackInfo callbackInfo, WorldServer var2, BlockPos pos, IBlockState var4, TileEntity var5, TileEntitySign sign) {
        String[] newLines = packet.getLines();
        MC_Player player = (MC_Player) this.player;

        if (player.hasPermission("rainbow.signcolor")) {
            for (int i = 0; i < 4; ++i) {
                newLines[i] = _DiwUtils.TranslateColorString(newLines[i], true);
            }
        }

        MC_EventInfo ei = new MC_EventInfo();

        MC_Location location = new MC_Location(pos.getX(), pos.getY(), pos.getZ(), this.player.dimension);

        ArrayList<String> replaceLines = new ArrayList<>();

        replaceLines.add(newLines[0]);
        replaceLines.add(newLines[1]);
        replaceLines.add(newLines[2]);
        replaceLines.add(newLines[3]);

        Hooks.onSignChanging(player, (MC_Sign) sign, location, replaceLines, ei);

        if (ei.isCancelled) {
            callbackInfo.cancel();
            return;
        }

        for (int var21 = 0; var21 < 4; ++var21) {
            newLines[var21] = replaceLines.get(var21);
        }

        String var22 = String.format("%s | %s | %s | %s",
                replaceLines.get(0), replaceLines.get(1),
                replaceLines.get(2), replaceLines.get(3));
        String var23 = String.format("------ [NEW SIGN by %s @ %s] --- %s",
                ((MC_Player) this.player).getName(),
                player.getLocation().toString(), var22);

        LogManager.getLogger().debug(var23);

        if (_DiwUtils.DoCensor) {
            for (int i = 0; i < 4; ++i) {
                String line = newLines[i];

                if (_DiwUtils.HasBadLanguage(line)) {
                    _DiwUtils.NotifyCensor(((MC_Player) this.player).getName(), "Sign: " + line);
                    System.out.println("* Censoring Sign Line: " + line);
                    line = "Censored";
                    newLines[i] = line;
                }
            }
        }
    }

    @Inject(method = "processUpdateSign", at = @At(value = "INVOKE", target = "net.minecraft.tileentity.TileEntitySign.markDirty()V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onChangedSign(CPacketUpdateSign packet, CallbackInfo callbackInfo, WorldServer var2, BlockPos pos, IBlockState var4, TileEntity var5, TileEntitySign sign) {
        MC_Location location = new MC_Location(pos.getX(), pos.getY(), pos.getZ(), player.dimension);

        Hooks.onSignChanged((MC_Player) player, (MC_Sign) sign, location);
    }

    @Overwrite
    public void processCustomPayload(CPacketCustomPayload var1) {
        PacketThreadUtil.checkThreadAndEnqueue(var1, (NetHandlerPlayServer)(Object)this, this.player.getServerWorld());
        String channelName = ((IMixinCPacketCustomPlayload) var1).getChannel().toString();
        PacketBuffer buf = ((IMixinCPacketCustomPlayload) var1).getData();
        byte[] data = new byte[buf.readableBytes()];
        int readerIndex = buf.readerIndex();
        buf.getBytes(readerIndex, data);
        Hooks.onPluginMessage(channelName, data, (MC_Player) player);
    }


    @Inject(method = "processUseEntity", at = @At(value = "INVOKE", target = "net.minecraft.entity.player.EntityPlayerMP.markPlayerActive()V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onEntityInteract(CPacketUseEntity var1, CallbackInfo callbackInfo, WorldServer world, Entity target) {
        if (var1.getAction() == CPacketUseEntity.Action.INTERACT || var1.getAction() == CPacketUseEntity.Action.INTERACT_AT) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptEntityInteract((MC_Player) player, (MC_Entity) target, ei);
            if (ei.isCancelled) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "processClickWindow", at = @At(value = "INVOKE", target = "net.minecraft.inventory.Container.slotClick(IILnet/minecraft/inventory/ClickType;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/item/ItemStack;"), cancellable = true)
    private void onClickWindow(CPacketClickWindow packet, CallbackInfo ci) {
        Container openContainer = player.openContainer;
        if (openContainer instanceof GUIContainer) {
            pendingTransactions.addKey(openContainer.windowId, packet.getActionNumber());
            player.connection.sendPacket(new SPacketConfirmTransaction(packet.getWindowId(), packet.getActionNumber(), false));
            openContainer.setCanCraft(player, false);
            openContainer.slotClick(packet.getSlotId(), packet.getUsedButton(), packet.getClickType(), player);
            if (openContainer == player.openContainer) {
                NonNullList<ItemStack> var8 = NonNullList.create();

                for (int var4 = 0; var4 < this.player.openContainer.inventorySlots.size(); ++var4) {
                    ItemStack var5 = this.player.openContainer.inventorySlots.get(var4).getStack();
                    ItemStack var6 = var5.isEmpty() ? ItemStack.EMPTY : var5;

                    var8.add(var6);
                }

                this.player.sendAllContents(this.player.openContainer, var8);
            }
            ci.cancel();
        }
    }

    @Inject(method = "disconnect", at = @At("HEAD"), cancellable = true) // disconnect
    private void onKick(ITextComponent reason, CallbackInfo callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onPlayerKick((MC_Player) player, reason.getString(), ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }
}
