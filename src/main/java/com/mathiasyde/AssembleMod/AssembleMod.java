package com.mathiasyde.AssembleMod;

import com.mathiasyde.AssembleMod.Blocks.HeaterBlock;
import com.mathiasyde.AssembleMod.Blocks.HeaterBlockEntity;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(AssembleMod.MODID)
public class AssembleMod {
    public static final String MODID = "assemble";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BlockRegistry = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BlockEntityTypesRegistry = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    public static final DeferredRegister<Item> ItemRegistry = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CreativeTabsRegistry = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);


    public static final RegistryObject<Block> HEATER_BLOCK = BlockRegistry.register(HeaterBlock.NAME, HeaterBlock::new);
    public static final RegistryObject<Block> BRONZE_BLOCK = BlockRegistry.register("bronze_block", () -> new Block(BlockBehaviour.Properties.of()));


    public static final RegistryObject<BlockEntityType<HeaterBlockEntity>> HEATER_BLOCK_ENTITY = BlockEntityTypesRegistry.register(HeaterBlock.NAME, () -> BlockEntityType.Builder.of(HeaterBlockEntity::new, HEATER_BLOCK.get()).build(null));
    public static final RegistryObject<Item> HEATER_BLOCK_ITEM = ItemRegistry.register(HeaterBlock.NAME, () -> new BlockItem(HEATER_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_BLOCK_ITEM = ItemRegistry.register("bronze_block", () -> new BlockItem(BRONZE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_INGOT_ITEM = ItemRegistry.register("bronze_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BRONZE_NUGGET_ITEM = ItemRegistry.register("bronze_nugget", () -> new Item(new Item.Properties()));

    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CreativeTabsRegistry.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> HEATER_BLOCK_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(HEATER_BLOCK_ITEM.get());
                output.accept(BRONZE_BLOCK_ITEM.get());
            }).build());

    public AssembleMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BlockRegistry.register(eventBus);
        BlockEntityTypesRegistry.register(eventBus);
        ItemRegistry.register(eventBus);
        CreativeTabsRegistry.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
