package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_Sign;
import com.google.common.base.Objects;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.projectrainbow.EmptyItemStack;
import org.projectrainbow.Hooks;
import org.projectrainbow.PluginHelper;
import org.projectrainbow._DiwUtils;
import org.projectrainbow._DynReward;
import org.projectrainbow._EmoteUtils;
import org.projectrainbow._JOT_OnlineTimeUtils;
import org.projectrainbow._JoeCommandStats;
import org.projectrainbow.interfaces.IMixinEntityPlayerMP;
import org.projectrainbow.interfaces.IMixinOutboundPacketSoundEffect;
import org.projectrainbow.interfaces.IMixinOutboundPacketSpawnPosition;
import org.projectrainbow.launch.Bootstrap;
import org.spongepowered.asm.mixin.Mixin;
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

@Mixin(NetHandlerPlayServer.class)
public class MixinNetHandlerPlayServer {

    @Shadow
    public EntityPlayerMP playerEntity;
    @Shadow
    private int teleportId;
    @Shadow
    private Vec3d targetPos;

    @Redirect(method = "processPlayer", at = @At(value = "INVOKE", target = "warn", remap = false))
    private void doLogWarning(Logger logger, String message) {
        if (!message.contains("is sending move packets too frequently") || !_DiwUtils.DoHideAnnoyingDefaultServerOutput) {
            logger.warn(message);
        }
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void onDisconnect(ITextComponent var1, CallbackInfo callback) {
        _JOT_OnlineTimeUtils.HandlePlayerLogout(playerEntity.getName(), playerEntity.getUniqueID());
        Hooks.onPlayerLogout(playerEntity.getName(), playerEntity.getUniqueID());
    }

    @Redirect(method = "onDisconnect", at = @At(value = "INVOKE", target = "info", ordinal = 0, remap = false))
    private void fixToString(Logger logger, String text, ITextComponent chatComponent) {
        logger.info(this.playerEntity.getName() + " lost connection: " + chatComponent.getUnformattedText());
    }

    @Inject(method = "processPlayerDigging", at = @At(value = "INVOKE", target = "net.minecraft.entity.player.EntityPlayerMP.markPlayerActive()V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onPacketPlayerDigging(CPacketPlayerDigging packet, CallbackInfo callback, WorldServer worldServer, BlockPos blockPos) {
        if (packet.getAction().equals(CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptBlockBreak((MC_Player) playerEntity, new MC_Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), playerEntity.dimension), ei);
            if (ei.isCancelled) {
                playerEntity.connection.sendPacket(new SPacketBlockChange(worldServer, blockPos));
                callback.cancel();
            }
        }
    }

    @Inject(method = "processRightClickBlock", at = @At(value = "INVOKE", target = "net.minecraft.entity.player.EntityPlayerMP.markPlayerActive()V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void processRightClickBlock(CPacketPlayerTryUseItemOnBlock packet, CallbackInfo callback, WorldServer worldServer,
                                              EnumHand hand, ItemStack itemStack, BlockPos blockPos,
                                              EnumFacing clickedFace) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptPlaceOrInteract((MC_Player) playerEntity, new MC_Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), playerEntity.dimension), PluginHelper.directionMap.get(clickedFace), PluginHelper.handMap.get(hand), ei);
        if (ei.isCancelled) {
            for (BlockPos pos : BlockPos.getAllInBox(blockPos.add(-1, -1, -1), blockPos.add(1, 1, 1))) {
                if (pos.getY() < 0 || pos.getY() > 255) continue;
                this.playerEntity.connection.sendPacket(new SPacketBlockChange(worldServer, pos));
            }
            callback.cancel();
        } else {
            _DynReward.HandleInteract((MC_Player) playerEntity, blockPos.getX(), blockPos.getY(), blockPos.getZ(), playerEntity.dimension);
        }
    }

    @Inject(method = "processPlayerBlockPlacement", at = @At(value = "INVOKE", target = "net.minecraft.entity.player.EntityPlayerMP.markPlayerActive()V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void onRightClickAir(CPacketPlayerTryUseItem packet, CallbackInfo callbackInfo, WorldServer world, EnumHand hand, ItemStack itemStack) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptItemUse((MC_Player) playerEntity, Objects.firstNonNull((MC_ItemStack) (Object) itemStack, EmptyItemStack.getInstance()), ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "processChatMessage", at = @At(value = "INVOKE", target = "net.minecraft.entity.player.EntityPlayerMP.markPlayerActive()V"), cancellable = true)
    public void onChat(CPacketChatMessage packet, CallbackInfo callbackInfo) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onPlayerInput((MC_Player) playerEntity, packet.getMessage(), ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        } else {
            if (!((MC_Player) playerEntity).isOp()) {
                String var11 = _JoeCommandStats.HandleNewCommand(playerEntity.getName(), packet.getMessage());

                if (var11 != null) {
                    ((MC_Player) playerEntity).kick(var11);
                    callbackInfo.cancel();
                    return;
                }

                if (_DiwUtils.HasBadLanguage(packet.getMessage())) {
                    _DiwUtils.NotifyCensor(playerEntity.getName(), packet.getMessage());
                    long var14 = _DiwUtils.IncreaseEventCount("CENSOR." + packet.getMessage());
                    String swearMsg = String.format(
                            "Kicking for Language (Censor #%d): %s: %s",
                            var14, packet.getMessage(), playerEntity.getName());

                    System.out.println(
                            "--------------------------------------------");
                    System.out.println(swearMsg);
                    System.out.println(
                            "--------------------------------------------");
                    ((MC_Player) playerEntity).kick(
                            "Censor #" + var14
                                    + ": Watch language, Children play");
                    callbackInfo.cancel();
                    return;
                }
            }

            if (packet.getMessage().startsWith("/")) {
                if (_EmoteUtils.HandleEmote((MC_Player) playerEntity, packet.getMessage().substring(1))) {
                    callbackInfo.cancel();
                    return;
                }
            }
        }
    }

    @ModifyVariable(method = "processChatMessage", at = @At(value = "INVOKE", target = "startsWith", remap = false))
    private String chatColor(String message) {
        if (message.startsWith("/") && !message.startsWith("/w") && !message.startsWith("/whisper") && !message.startsWith("/msg"))
            return message;
        if (((MC_Player) playerEntity).hasPermission("rainbow.chatcolor")) {
            return _DiwUtils.TranslateChatString(message, ((MC_Player) playerEntity).isOp());
        }
        return message;
    }

    @Inject(method = "handleSlashCommand", at = @At("HEAD"))
    private void onCommand(String command, CallbackInfo callbackInfo) {
        if (!command.startsWith("/login")) {
            Bootstrap.logger.info("" + ((MC_Player) playerEntity).getName() + ": " + command);
        }
    }

    @Inject(method = "setPlayerLocation(DDDFFLjava/util/Set;)V", at = @At("HEAD"), cancellable = true)
    private void hookTeleport(double x, double y, double z, float yaw, float pitch, Set<SPacketPlayerPosLook.EnumFlags> flags, CallbackInfo callbackInfo) {
        double dx = flags.contains(SPacketPlayerPosLook.EnumFlags.X) ? x : playerEntity.posX - x;
        double dy = flags.contains(SPacketPlayerPosLook.EnumFlags.Y) ? y : playerEntity.posY - y;
        double dz = flags.contains(SPacketPlayerPosLook.EnumFlags.Z) ? z : playerEntity.posZ - z;
        float finalYaw = flags.contains(SPacketPlayerPosLook.EnumFlags.Y_ROT) ? yaw + playerEntity.rotationYaw : yaw;
        float finalPitch = flags.contains(SPacketPlayerPosLook.EnumFlags.X_ROT) ? pitch + playerEntity.rotationPitch : pitch;
        // teleport
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptPlayerTeleport((MC_Player) playerEntity, new MC_Location(playerEntity.posX + dx, playerEntity.posY + dy, playerEntity.posZ + dz, playerEntity.dimension, finalYaw, finalPitch), ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();

            if (++this.teleportId == 2147483647) {
                this.teleportId = 0;
            }

            playerEntity.connection.sendPacket(new SPacketPlayerPosLook(playerEntity.posX, playerEntity.posY, playerEntity.posZ, playerEntity.rotationYaw, playerEntity.rotationPitch, Collections.<SPacketPlayerPosLook.EnumFlags>emptySet(), this.teleportId));
        }
    }

    @Inject(method = "processPlayer", at = @At(value = "FIELD", target = "net.minecraft.network.NetHandlerPlayServer.serverController:Lnet/minecraft/server/MinecraftServer;", ordinal = 0), cancellable = true)
    private void onPlayerMove(CPacketPlayer packet, CallbackInfo callbackInfo) {
        double x = packet.getX(this.playerEntity.posX);
        double y = packet.getY(this.playerEntity.posY);
        double z = packet.getZ(this.playerEntity.posZ);
        float yaw = packet.getYaw(this.playerEntity.rotationYaw);
        float pitch = packet.getPitch(this.playerEntity.rotationPitch);
        double oldX = this.playerEntity.posX;
        double oldY = this.playerEntity.posY;
        double oldZ = this.playerEntity.posZ;
        float oldYaw = this.playerEntity.rotationYaw;
        float oldPitch = this.playerEntity.rotationPitch;

        if (Math.abs(x - oldX) > 1E-4 || Math.abs(y - oldY) > 1E-4 || Math.abs(z - oldZ) > 1E-4 || Math.abs(yaw - oldYaw) > 1E-4 || Math.abs(pitch - oldPitch) > 1E-4) {

            MC_EventInfo ei = new MC_EventInfo();
            MC_Player player = (MC_Player) playerEntity;

            MC_Location from = player.getLocation();
            MC_Location to = new MC_Location(x, y, z, from.dimension, yaw, pitch);

            Hooks.onAttemptPlayerMove(player, from, to, ei);

            if (ei.isCancelled) {
                this.targetPos = new Vec3d(oldX, oldY, oldZ);
                playerEntity.connection.sendPacket(new SPacketPlayerPosLook(oldX, oldY, oldZ, oldYaw, oldPitch, Collections.<SPacketPlayerPosLook.EnumFlags>emptySet(), ++this.teleportId));
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    private void onPacketSent(final Packet<?> argPacket, CallbackInfo callbackInfo) {
        if (argPacket instanceof SPacketSpawnPosition) {
            BlockPos pos = ((IMixinOutboundPacketSpawnPosition) argPacket).getPos();

            ((IMixinEntityPlayerMP) playerEntity).onCompassTargetUpdated(new MC_Location(
                    (double) pos.getX(), (double) pos.getY(),
                    (double) pos.getZ(), this.playerEntity.dimension));
        }

        if (argPacket instanceof IMixinOutboundPacketSoundEffect) {
            IMixinOutboundPacketSoundEffect packet = (IMixinOutboundPacketSoundEffect) argPacket;
            MC_EventInfo ei = new MC_EventInfo();
            MC_Location loc = new MC_Location(
                    (double) (packet.getX() / 8), (double) (packet.getY() / 8),
                    (double) (packet.getZ() / 8), this.playerEntity.dimension);


            Hooks.onPacketSoundEffect((MC_Player) playerEntity, packet.getSoundName(), loc, ei);

            if (ei.isCancelled) {
                callbackInfo.cancel();
            }
        }
    }

    @Redirect(method = "processUpdateSign", at = @At(value = "INVOKE", target = "net.minecraft.util.text.TextFormatting.getTextWithoutFormattingCodes(Ljava/lang/String;)Ljava/lang/String;"))
    private String stripColorCodes(String s) {
        if (((MC_Player) playerEntity).hasPermission("rainbow.signcolor")) {
            return s;
        } else {
            return TextFormatting.getTextWithoutFormattingCodes(s);
        }
    }

    @Inject(method = "processUpdateSign", at = @At(value = "INVOKE", target = "net.minecraft.network.play.client.CPacketUpdateSign.getLines()[Ljava/lang/String;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void onChangingSign(CPacketUpdateSign packet, CallbackInfo callbackInfo, WorldServer var2, BlockPos pos, IBlockState var4, TileEntity var5, TileEntitySign sign) {
        String[] newLines = packet.getLines();
        MC_Player player = (MC_Player) playerEntity;

        if (player.hasPermission("rainbow.signcolor")) {
            for (int i = 0; i < 4; ++i) {
                newLines[i] = _DiwUtils.TranslateColorString(newLines[i], true);
            }
        }

        MC_EventInfo ei = new MC_EventInfo();

        MC_Location location = new MC_Location(pos.getX(), pos.getY(), pos.getZ(), playerEntity.dimension);

        ArrayList<String> replaceLines = new ArrayList<String>();

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
                this.playerEntity.getName(),
                player.getLocation().toString(), var22);

        LogManager.getLogger().debug(var23);

        if (_DiwUtils.DoCensor) {
            for (int i = 0; i < 4; ++i) {
                String line = newLines[i];

                if (_DiwUtils.HasBadLanguage(line)) {
                    _DiwUtils.NotifyCensor(this.playerEntity.getName(), "Sign: " + line);
                    System.out.println("* Censoring Sign Line: " + line);
                    line = "Censored";
                    newLines[i] = line;
                }
            }
        }
    }

    @Inject(method = "processUpdateSign", at = @At(value = "INVOKE", target = "net.minecraft.tileentity.TileEntitySign.markDirty()V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onChangedSign(CPacketUpdateSign packet, CallbackInfo callbackInfo, WorldServer var2, BlockPos pos, IBlockState var4, TileEntity var5, TileEntitySign sign) {
        MC_Location location = new MC_Location(pos.getX(), pos.getY(), pos.getZ(), playerEntity.dimension);

        Hooks.onSignChanged((MC_Player) playerEntity, (MC_Sign) sign, location);
    }

    @Inject(method = "processCustomPayload", at = @At(value = "INVOKE", target = "net.minecraft.item.ItemStack.setTagInfo(Ljava/lang/String;Lnet/minecraft/nbt/NBTBase;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onBookChange(CPacketCustomPayload packet, CallbackInfo callbackInfo, String var2, PacketBuffer var3, ItemStack newBook, ItemStack oldBook) {
        NBTTagList tagList = newBook.getTagCompound().getTagList("pages", 8);
        ArrayList<String> pages = new ArrayList<String>();
        for (int j = 0; j < tagList.tagCount(); j++) {
            pages.add(tagList.getStringTagAt(j));
        }
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptBookChange((MC_Player) playerEntity, pages, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
            return;
        }

        while (tagList.tagCount() > 0) {
            tagList.removeTag(0);
        }
        for (String page : pages) {
            tagList.appendTag(new NBTTagString(page));
        }

        newBook.setTagInfo("pages", tagList);
    }

    @Inject(method = "processCustomPayload", at = @At(value = "INVOKE", target = "net.minecraft.item.ItemStack.setTagInfo(Ljava/lang/String;Lnet/minecraft/nbt/NBTBase;)V", ordinal = 3), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onBookSign(CPacketCustomPayload packet, CallbackInfo callbackInfo, String var2, PacketBuffer var3, ItemStack newBook, ItemStack oldBook) {
        NBTTagList tagList = newBook.getTagCompound().getTagList("pages", 8);
        ArrayList<String> pages = new ArrayList<String>();
        pages.add(oldBook.getTagCompound().getString("author"));
        pages.add(oldBook.getTagCompound().getString("title"));
        for (int j = 0; j < tagList.tagCount(); j++) {
            pages.add(tagList.getStringTagAt(j));
        }
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptBookChange((MC_Player) playerEntity, pages, ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
            oldBook.getTagCompound().removeTag("author");
            oldBook.getTagCompound().removeTag("title");
            return;
        }

        oldBook.setTagInfo("author", new NBTTagString(pages.remove(0)));
        oldBook.setTagInfo("title", new NBTTagString(pages.remove(0)));

        while (tagList.tagCount() > 0) {
            tagList.removeTag(0);
        }
        for (String page : pages) {
            tagList.appendTag(new NBTTagString(page));
        }

        newBook.setTagInfo("pages", tagList);
    }

    @Inject(method = "processUseEntity", at = @At(value = "INVOKE", target = "net.minecraft.entity.player.EntityPlayerMP.markPlayerActive()V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onEntityInteract(CPacketUseEntity var1, CallbackInfo callbackInfo, WorldServer world, Entity target) {
        if (var1.getAction() == CPacketUseEntity.Action.INTERACT || var1.getAction() == CPacketUseEntity.Action.INTERACT_AT) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptEntityInteract((MC_Player) playerEntity, (MC_Entity) target, ei);
            if (ei.isCancelled) {
                callbackInfo.cancel();
            }
        }
    }
}
