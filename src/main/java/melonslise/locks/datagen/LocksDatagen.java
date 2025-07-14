package melonslise.locks.datagen;

import melonslise.locks.datagen.recipes.LocksRecipesGen;
import melonslise.locks.datagen.tags.LocksBlockTagsGen;
import melonslise.locks.datagen.tags.LocksEnchantmentTagsGen;
import melonslise.locks.datagen.tags.LocksItemTagsGen;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class LocksDatagen  implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(LocksRecipesGen::new);
        pack.addProvider(LocksBlockTagsGen::new);
        pack.addProvider(LocksEnchantmentTagsGen::new);
        pack.addProvider(LocksItemTagsGen::new);
    }

}