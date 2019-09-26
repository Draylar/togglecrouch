package com.github.draylar.togglecrouch;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.mixin.client.keybinding.KeyCodeAccessor;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class ToggleCrouch implements ModInitializer
{
	private static final String MODID = "togglecrouch";

	private static FabricKeyBinding toggleCrouch = FabricKeyBinding.Builder.create(
			new Identifier(MODID, "toggle"),
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_C,
			"Togglecrouch"
	).build();

	private static boolean isCrouching = false;
	private static boolean wasJustOn = false;

	@Override
	public void onInitialize()
	{
		KeyBindingRegistry.INSTANCE.register(toggleCrouch);

		ClientTickCallback.EVENT.register((client) -> {
			if(client.options.keySneak.wasPressed()) {
				isCrouching = false;
				wasJustOn = false;
			}

			if(client.player != null) {
				if (toggleCrouch.wasPressed()) {
					isCrouching = !isCrouching;
				}

				if (isCrouching) {
					KeyBinding.setKeyPressed(getConfiguredKeyCode(client.options.keySneak), true);
					wasJustOn = true;
				} else {
					if(client.player.isSneaking() && wasJustOn) {
						KeyBinding.setKeyPressed(getConfiguredKeyCode(client.options.keySneak), false);
						wasJustOn = false;
					}
				}
			}
		});
	}

	private static InputUtil.KeyCode getConfiguredKeyCode(KeyBinding keyBinding) {
		return ((KeyCodeAccessor) keyBinding).getKeyCode();
	}
}
