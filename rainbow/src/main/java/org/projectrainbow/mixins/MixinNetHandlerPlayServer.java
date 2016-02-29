package org.projectrainbow.mixins;

import PluginReference.MC_Entity;
import PluginReference.MC_EventInfo;
import PluginReference.MC_ItemStack;
import PluginReference.MC_Location;
import PluginReference.MC_Player;
import PluginReference.MC_Sign;
import com.google.common.base.Objects;
import net.minecraft.src.BlockPos;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EnumChatFormatting;
import net.minecraft.src.EnumFacing;
import net.minecraft.src.EnumHand;
import net.minecraft.src.IBlockState;
import net.minecraft.src.IChatComponent;
import net.minecraft.src.InboundPacketChatMessage;
import net.minecraft.src.InboundPacketCustomPayload;
import net.minecraft.src.InboundPacketPlayer;
import net.minecraft.src.InboundPacketPlayerBlockPlacement;
import net.minecraft.src.InboundPacketPlayerDigging;
import net.minecraft.src.InboundPacketUpdateSign;
import net.minecraft.src.InboundPacketUseEntity;
import net.minecraft.src.InboundPacketUseItem;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagString;
import net.minecraft.src.NetHandlerPlayServer;
import net.minecraft.src.OutboundPacketPlayerPosLook;
import net.minecraft.src.OutboundPacketSpawnPosition;
import net.minecraft.src.Packet;
import net.minecraft.src.PacketBuffer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySign;
import net.minecraft.src.WorldServer;
import net.minecraft.src.fu;
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
    private int z;

    @Redirect(method = "processPlayer", at = @At(value = "INVOKE", target = "warn", remap = false))
    private void doLogWarning(Logger logger, String message) {
        if (!message.contains("is sending move packets too frequently") || !_DiwUtils.DoHideAnnoyingDefaultServerOutput) {
            logger.warn(message);
        }
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void onDisconnect(IChatComponent var1, CallbackInfo callback) {
        _JOT_OnlineTimeUtils.HandlePlayerLogout(playerEntity.getName(), playerEntity.getUniqueID());
        Hooks.onPlayerLogout(playerEntity.getName(), playerEntity.getUniqueID());
    }

    @Redirect(method = "onDisconnect", at = @At(value = "INVOKE", target = "info", ordinal = 0, remap = false))
    private void fixToString(Logger logger, String text, IChatComponent chatComponent) {
        logger.info(this.playerEntity.getName() + " lost connection: " + chatComponent.getUnformattedText());
    }

    @Inject(method = "a(Lnet/minecraft/src/InboundPacketPlayerDigging;)V", at = @At(value = "INVOKE", target = "net.minecraft.src.EntityPlayerMP.markPlayerActive()V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onPacketPlayerDigging(InboundPacketPlayerDigging packet, CallbackInfo callback, WorldServer worldServer, BlockPos blockPos) {
        if (packet.c().equals(InboundPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptBlockBreak((MC_Player) playerEntity, new MC_Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), playerEntity.dimension), ei);
            if (ei.isCancelled) {
                playerEntity.playerNetServerHandler.sendPacket(new fu(worldServer, blockPos)); // OutboundPacketBlockChange
                callback.cancel();
            }
        }
    }

    @Inject(method = "processPlayerBlockPlacement", at = @At(value = "INVOKE", target = "net.minecraft.src.EntityPlayerMP.markPlayerActive()V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onPacketPlayerBlockPlacement(InboundPacketPlayerBlockPlacement packet, CallbackInfo callback, WorldServer worldServer,
                                              EnumHand hand, ItemStack itemStack, BlockPos blockPos,
                                              EnumFacing clickedFace) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptPlaceOrInteract((MC_Player) playerEntity, new MC_Location(blockPos.getX(), blockPos.getY(), blockPos.getZ(), playerEntity.dimension), PluginHelper.directionMap.get(clickedFace), PluginHelper.handMap.get(hand), ei);
        if (ei.isCancelled) {
            for (BlockPos pos : BlockPos.getAllInBox(blockPos.add(-1, -1, -1), blockPos.add(1, 1, 1))) {
                if (pos.getY() < 0 || pos.getY() > 255) continue;
                this.playerEntity.playerNetServerHandler.sendPacket(new fu(worldServer, pos)); // OutboundPacketBlockChange
            }
            callback.cancel();
        } else {
            _DynReward.HandleInteract((MC_Player) playerEntity, blockPos.getX(), blockPos.getY(), blockPos.getZ(), playerEntity.dimension);
        }
    }

    @Inject(method = "a(Lnet/minecraft/src/InboundPacketUseItem;)V", at = @At(value = "INVOKE", target = "net.minecraft.src.EntityPlayerMP.markPlayerActive()V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void onRightClickAir(InboundPacketUseItem packet, CallbackInfo callbackInfo, WorldServer world, EnumHand hand, ItemStack itemStack) {
        MC_EventInfo ei = new MC_EventInfo();
        Hooks.onAttemptItemUse((MC_Player) playerEntity, Objects.firstNonNull((MC_ItemStack) (Object) itemStack, EmptyItemStack.getInstance()), ei);
        if (ei.isCancelled) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "processChatMessage", at = @At(value = "INVOKE", target = "net.minecraft.src.EntityPlayerMP.markPlayerActive()V"), cancellable = true)
    public void onChat(InboundPacketChatMessage packet, CallbackInfo callbackInfo) {
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
                if (_EmoteUtils.HandleEmote((MC_Player) playerEntity, packet.getMessage())) {
                    callbackInfo.cancel();
                    return;
                }
            }
        }
    }

    @ModifyVariable(method = "processChatMessage", at = @At(value = "INVOKE", target = "startsWith"))
    private String chatColor(String message) {
        if (message.startsWith("/") && !message.startsWith("/w") && !message.startsWith("/whisper") && !message.startsWith("/msg"))
            return message;
        if (((MC_Player)playerEntity).hasPermission("rainbow.chatcolor")) {
            return _DiwUtils.TranslateChatString(message, ((MC_Player)playerEntity).isOp());
        }
        return message;
    }

    @Inject(method = "setPlayerLocation(DDDFFLjava/util/Set;)V", at = @At("HEAD"), cancellable = true)
    private void hookTeleport(double x, double y, double z, float yaw, float pitch, Set<OutboundPacketPlayerPosLook.EnumFlags> flags, CallbackInfo callbackInfo) {
        double dx = flags.contains(OutboundPacketPlayerPosLook.EnumFlags.X) ? x : playerEntity.posX - x;
        double dy = flags.contains(OutboundPacketPlayerPosLook.EnumFlags.Y) ? y : playerEntity.posY - y;
        double dz = flags.contains(OutboundPacketPlayerPosLook.EnumFlags.Z) ? z : playerEntity.posZ - z;
        float finalYaw = flags.contains(OutboundPacketPlayerPosLook.EnumFlags.Y_ROT) ? yaw + playerEntity.rotationYaw : yaw;
        float finalPitch = flags.contains(OutboundPacketPlayerPosLook.EnumFlags.X_ROT) ? pitch + playerEntity.rotationPitch : pitch;
        if (dx * dx + dy * dy + dz * dz > 4) {
            // teleport
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptPlayerTeleport((MC_Player) playerEntity, new MC_Location(playerEntity.posX + dx, playerEntity.posY + dy, playerEntity.posZ + dz, playerEntity.dimension, finalYaw, finalPitch), ei);
            if (ei.isCancelled) {
                callbackInfo.cancel();

                if (++this.z == 2147483647) {
                    this.z = 0;
                }

                playerEntity.playerNetServerHandler.sendPacket(new OutboundPacketPlayerPosLook(playerEntity.posX, playerEntity.posY, playerEntity.posZ, playerEntity.rotationYaw, playerEntity.rotationPitch, Collections.<OutboundPacketPlayerPosLook.EnumFlags>emptySet(), this.z));
            }
        }
    }

    @Inject(method = "processPlayer", at = @At(value = "FIELD", target = "net.minecraft.src.NetHandlerPlayServer.serverController:Lnet/minecraft/server/MinecraftServer;", ordinal = 0), cancellable = true)
    private void onPlayerMove(InboundPacketPlayer packet, CallbackInfo callbackInfo) {
        double x = packet.a(this.playerEntity.posX);
        double y = packet.b(this.playerEntity.posY);
        double z = packet.c(this.playerEntity.posZ);
        float yaw = packet.a(this.playerEntity.rotationYaw);
        float pitch = packet.b(this.playerEntity.rotationPitch);
        double oldX = x - this.playerEntity.posX;
        double oldY = y - this.playerEntity.posY;
        double oldZ = z - this.playerEntity.posZ;
        float oldYaw = this.playerEntity.rotationYaw;
        float oldPitch = this.playerEntity.rotationPitch;

        if (Math.abs(x - oldX) > 1E-4 || Math.abs(y - oldY) > 1E-4 || Math.abs(z - oldZ) > 1E-4 || Math.abs(yaw - oldYaw) > 1E-4 || Math.abs(pitch - oldPitch) > 1E-4) {

            MC_EventInfo ei = new MC_EventInfo();
            MC_Player player = (MC_Player) playerEntity;

            MC_Location from = player.getLocation();
            MC_Location to = new MC_Location(x, y, z, from.dimension, yaw, pitch);

            Hooks.onAttemptPlayerMove(player, from, to, ei);

            if (ei.isCancelled) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    private void onPacketSent(final Packet<?> argPacket, CallbackInfo callbackInfo) {
        if (argPacket instanceof OutboundPacketSpawnPosition) {
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

    @Redirect(method = "processUpdateSign", at = @At(value = "INVOKE", target = "net.minecraft.src.EnumChatFormatting.a(Ljava/lang/String;)Ljava/lang/String;"))
    private String stripColorCodes(String s) {
        if (((MC_Player) playerEntity).hasPermission("rainbow.signcolor")) {
            return s;
        } else {
            return EnumChatFormatting.a(s);
        }
    }

    @Inject(method = "processUpdateSign", at = @At(value = "INVOKE", target = "net.minecraft.src.InboundPacketUpdateSign.b()[Ljava/lang/String;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void onChangingSign(InboundPacketUpdateSign packet, CallbackInfo callbackInfo, WorldServer var2, BlockPos pos, IBlockState var4, TileEntity var5, TileEntitySign sign) {
        String[] newLines = packet.b();
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

    @Inject(method = "processUpdateSign", at = @At(value = "INVOKE", target = "net.minecraft.src.TileEntitySign.markDirty()V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onChangedSign(InboundPacketUpdateSign packet, CallbackInfo callbackInfo, WorldServer var2, BlockPos pos, IBlockState var4, TileEntity var5, TileEntitySign sign) {
        MC_Location location = new MC_Location(pos.getX(), pos.getY(), pos.getZ(), playerEntity.dimension);

        Hooks.onSignChanged((MC_Player) playerEntity, (MC_Sign) sign, location);
    }

    @Inject(method = "processVanilla250Packet", at = @At(value = "INVOKE", target = "net.minecraft.src.ItemStack.setTagInfo(Ljava/lang/String;Lnet/minecraft/src/NBTBase;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onBookChange(InboundPacketCustomPayload packet, CallbackInfo callbackInfo, String var2, PacketBuffer var3, ItemStack newBook, ItemStack oldBook) {
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

    @Inject(method = "processVanilla250Packet", at = @At(value = "INVOKE", target = "net.minecraft.src.ItemStack.setTagInfo(Ljava/lang/String;Lnet/minecraft/src/NBTBase;)V", ordinal = 3), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onBookSign(InboundPacketCustomPayload packet, CallbackInfo callbackInfo, String var2, PacketBuffer var3, ItemStack newBook, ItemStack oldBook) {
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

    @Inject(method = "processUseEntity", at = @At(value = "INVOKE", target = "net.minecraft.src.EntityPlayerMP.markPlayerActive()V"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onEntityInteract(InboundPacketUseEntity var1, CallbackInfo callbackInfo, WorldServer world, Entity target) {
        if (var1.getAction() == InboundPacketUseEntity.Action.INTERACT || var1.getAction() == InboundPacketUseEntity.Action.INTERACT_AT) {
            MC_EventInfo ei = new MC_EventInfo();
            Hooks.onAttemptEntityInteract((MC_Player) playerEntity, (MC_Entity) target, ei);
            if (ei.isCancelled) {
                callbackInfo.cancel();
            }
        }
    }
}
