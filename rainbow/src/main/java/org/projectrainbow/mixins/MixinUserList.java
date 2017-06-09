package org.projectrainbow.mixins;

import net.minecraft.server.management.UserList;
import net.minecraft.server.management.UserListBans;
import net.minecraft.server.management.UserListIPBans;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

// fixes https://bugs.mojang.com/browse/MC-84786
// todo 1.13 check whether mojang fixed the issue
@Mixin(UserList.class)
public abstract class MixinUserList {

    @Shadow
    protected abstract String getObjectKey(Object var1);

    @Shadow
    private void removeExpired() {}

    @Redirect(method = "removeExpired", at = @At(value = "INVOKE", target = "java.util.Map.remove(Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
    private Object fixRemoveExpired(Map<Object, Object> map, Object key) {
        return map.remove(getObjectKey(key));
    }

    @Inject(method = "hasEntry", at = @At("HEAD"))
    private void fixHasEntry(Object o, CallbackInfoReturnable cir) {
        if (((Object) this) instanceof UserListBans || ((Object) this) instanceof UserListIPBans) {
            removeExpired();
        }
    }
}
