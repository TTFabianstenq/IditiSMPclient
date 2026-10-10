package dev.iditismp;

import dev.iditismp.config.ConfigManager;
import dev.iditismp.gui.ClickGuiScreen;
import dev.iditismp.module.ModuleManager;
import dev.iditismp.render.HudRenderer;
import dev.iditismp.render.WorldEspRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.AfterEntities;
import net.minecraft.util.Identifier;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IditiSMPClient implements ClientModInitializer {
   public static final String MOD_ID = "iditismp-client";
   public static final Logger LOGGER = LoggerFactory.getLogger("iditismp-client");
   public static IditiSMPClient INSTANCE;
   private ModuleManager moduleManager;
   private ConfigManager configManager;
   private HudRenderer hudRenderer;
   private boolean lastRightShift = false;
   private int saveTick = 0;

   public void onInitializeClient() {
      INSTANCE = this;
      this.moduleManager = new ModuleManager();
      this.configManager = new ConfigManager();
      this.hudRenderer = new HudRenderer();
      this.configManager.load();
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)client -> {
         if (++this.saveTick % 200 == 0) {
            try {
               this.configManager.saveSoon();
            } catch (Throwable var8) {
            }
         }

         if (client.player != null) {
            long window = client.getWindow().getHandle();
            boolean pressed = GLFW.glfwGetKey(window, 344) == 1;
            if (pressed && !this.lastRightShift) {
               if (client.currentScreen instanceof ClickGuiScreen) {
                  try {
                     this.configManager.save();
                  } catch (Throwable var7) {
                  }

                  client.setScreen(null);
               } else if (client.currentScreen == null) {
                  client.setScreen(new ClickGuiScreen());
               }
            }

            this.lastRightShift = pressed;

            try {
               this.moduleManager.onTick(client);
            } catch (Throwable var6) {
               LOGGER.error("tick", var6);
            }
         }
      });
      HudElementRegistry.attachElementAfter(
         VanillaHudElements.MISC_OVERLAYS, Identifier.of("iditismp-client", "overlay"), (context, tickCounter) -> {
            try {
               if (INSTANCE != null && MinecraftClient.getInstance().player != null) {
                  this.hudRenderer.render(context, 0.0F);
               }
            } catch (Throwable var4) {
            }
         }
      );
      WorldRenderEvents.AFTER_ENTITIES.register((AfterEntities)ctx -> {
         try {
            WorldEspRenderer.render(ctx);
         } catch (Throwable var2) {
         }
      });
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
         try {
            this.configManager.save();
         } catch (Throwable var2) {
         }
      }, "IditiSMP-ConfigSave"));
      LOGGER.info("IditiSMP Client 1.0.0 ready. Modules: " + this.moduleManager.getModules().size());
   }

   public ModuleManager getModuleManager() {
      return this.moduleManager;
   }

   public ConfigManager getConfigManager() {
      return this.configManager;
   }

   public HudRenderer getHudRenderer() {
      return this.hudRenderer;
   }
}
