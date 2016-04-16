package ovh.corail.recycler.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import ovh.corail.recycler.handler.ConfigurationHandler;
import ovh.corail.recycler.handler.GuiHandler;
import ovh.corail.recycler.handler.PacketHandler;
import ovh.corail.recycler.recycling.JsonRecyclingRecipe;
import ovh.corail.recycler.recycling.RecyclingManager;
import ovh.corail.recycler.tileentity.TileEntityRecycler;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) throws IOException {
		/** config */
		ConfigurationHandler.config = new Configuration(event.getSuggestedConfigurationFile());
		ConfigurationHandler.config.load();
		ConfigurationHandler.refreshConfig();
		/** register */
		GameRegistry.registerItem(Main.iron_nugget);
		GameRegistry.registerItem(Main.diamond_nugget);
		GameRegistry.registerItem(Main.diamond_disk);
		GameRegistry.registerBlock(Main.recycler);
		GameRegistry.registerTileEntity(TileEntityRecycler.class, "inventoryRecycler");
		/** new recipes for crafting table */
		MainUtil.getNewRecipes();
		/** messages handler */
		PacketHandler.init();
		/** load recycling recipes from json */
		File recyclingRecipesFile = new File(event.getModConfigurationDirectory(), "recyclingRecipes.json");
		if (!recyclingRecipesFile.exists()) {
			recyclingRecipesFile.createNewFile();
			FileWriter fw = new FileWriter(recyclingRecipesFile);
			List<JsonRecyclingRecipe> jsonRecipesList = RecyclingManager.getJsonRecyclingRecipes();
			fw.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonRecipesList));
			fw.close();
		}
		List<JsonRecyclingRecipe> jsonRecipesList = new Gson().fromJson(new BufferedReader(new FileReader(recyclingRecipesFile)),
			new TypeToken<List<JsonRecyclingRecipe>>() {}.getType());
		RecyclingManager.loadJsonRecipes(jsonRecipesList);
	}

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiHandler());
	}

	public void postInit(FMLPostInitializationEvent event) {
	}
	
	protected void initRender() {
	}

	protected void render(Item item) {	
	}

	protected void render(Block block) {	
	}
	
	
}
