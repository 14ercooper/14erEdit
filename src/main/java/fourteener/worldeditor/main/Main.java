package fourteener.worldeditor.main;

import org.bukkit.plugin.java.JavaPlugin;

import fourteener.worldeditor.commands.CommandFx;
import fourteener.worldeditor.commands.fx.SelectionWandListener;

public class Main extends JavaPlugin {
	@Override
	public void onEnable () {
		// Register commands
		this.getCommand("fx").setExecutor(new CommandFx());
		
		// Register listeners
		getServer().getPluginManager().registerEvents(new SelectionWandListener(), this);
	}
	
	@Override
	public void onDisable () {
		
	}
}
