package fourteener.worldeditor.worldeditor.scripts.bundled;

import fourteener.worldeditor.main.Main;
import fourteener.worldeditor.worldeditor.scripts.Craftscript;
import fourteener.worldeditor.worldeditor.selection.SelectionWand;
import fourteener.worldeditor.worldeditor.selection.SelectionWandListener;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class ScriptBridge extends Craftscript {
    @Override
    public List<BlockState> perform(LinkedList<String> args, Player player, String label) {
        SelectionWand wand = null;
        // Make sure this wand comes from the player
        for (SelectionWand s : SelectionWandListener.wands){
            if (s.owner.equals(player)){
                wand = s;
                break;
            }
        }

        double[] mostNegative = wand.manager.getMostNegativeCorner();
        double[] mostPositive = wand.manager.getMostPositiveCorner();
        double[] midpoint = new double[]{0, 0, 0};
        for (int i = 0; i<=2; i++){
            midpoint[i] = (mostPositive[i] + mostNegative[i])/2;
        }
        Main.logDebug("Midpoint is x=" + midpoint[0] + ", y=" + midpoint[1] + ", z=" + midpoint[2]);



        return null;
    }
}
