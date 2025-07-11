package melonslise.locks;

import melonslise.locks.client.init.LocksItemModelsProperties;
import melonslise.locks.client.init.LocksNetworkClient;
import melonslise.locks.client.init.LocksScreens;
import melonslise.locks.common.config.LocksConfig;
import melonslise.locks.common.event.LocksEvents;
import melonslise.locks.common.init.*;
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
		LocksItems.register();
		LocksEnchantments.register();
		LocksSoundEvents.register();
		LocksContainerTypes.register();
		LocksRecipeSerializers.register();
		LocksVillagerTrades.register();
		LocksEvents.register();
		LocksNetwork.register();
		LocksFeatures.register();
		LocksLootParamSets.register();
	}

	@Override
	public void onInitializeClient() {
		LocksScreens.register();
		LocksItemModelsProperties.register();
		LocksNetworkClient.register();
	}
}