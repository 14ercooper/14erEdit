package fourteener.worldeditor.main;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import fourteener.worldeditor.commands.CommandFx;
import fourteener.worldeditor.worldeditor.brush.BrushListener;
import fourteener.worldeditor.worldeditor.selection.SelectionWandListener;
import fourteener.worldeditor.worldeditor.undo.UndoCommand;

public class Main extends JavaPlugin {
	// Global variables
	public static World world;
	
	// For debugging
	public static boolean isDebug = true;
	
	@Override
	public void onEnable () {
		// Register commands
		this.getCommand("fx").setExecutor(new CommandFx());
		UndoCommand undoCmd = new UndoCommand();
		this.getCommand("undo").setExecutor(undoCmd);
		this.getCommand("redo").setExecutor(undoCmd);
		
		// Register listeners
		getServer().getPluginManager().registerEvents(new SelectionWandListener(), this);
		getServer().getPluginManager().registerEvents(new BrushListener(), this);
		
		// These are needed by the plugin, but should only be loaded once as they are very slow to load
		world = Bukkit.getWorlds().get(0);
	}
	
	@Override
	public void onDisable () {
		
	}
}
