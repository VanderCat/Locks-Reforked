package melonslise.locks;

import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import melonslise.locks.client.init.LocksItemModelsProperties;
import melonslise.locks.client.init.LocksScreens;
import melonslise.locks.config.LocksConfig;
import melonslise.locks.events.LocksEvents;
import melonslise.locks.init.*;
import melonslise.locks.network.LocksNetwork;
import melonslise.locks.events.LocksRenderEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * MOD init and registration.
 */
public final class Locks implements ModInitializer, ClientModInitializer {
	public static final LocksConfig CONFIG = LocksConfig.createAndLoad();
	public static final String ID = "locks";

	public static final Logger LOGGER = LogManager.getLogger("Locks");

	@Override
	public void onInitialize() {
		//EnumModifier.run();
		//TODO: run the EnumModifier class to add enchantments to the game
		FieldRegistrationHandler.register(LocksItems.class, ID, false);
		LocksItems.GROUP.initialize();
		FieldRegistrationHandler.register(LocksEnchantments.class, ID, false);
		LocksSoundEvents.register();
		LocksContainerTypes.register();
		FieldRegistrationHandler.register(LocksRecipeSerializers.class, ID, false);
		LocksVillagerTrades.register();
		LocksEvents.register();
		LocksNetwork.registerServerbound();
		LocksFeatures.register();
		LocksLootParamSets.register();
	}

	@Override
	public void onInitializeClient() {
		LocksScreens.register();
		LocksItemModelsProperties.register();
		LocksNetwork.registerClientbound();
		LocksRenderEvents.register();
	}
}