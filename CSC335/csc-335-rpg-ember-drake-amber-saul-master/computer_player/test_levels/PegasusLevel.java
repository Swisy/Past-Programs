package computer_player.test_levels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import model.StrategyGameState;
import model.Team;
import onboard.BlockedTile;
import onboard.InvalidMoveException;
import onboard.Knight;
import onboard.OpenTile;
import onboard.Pegasus;
import onboard.Tile;

public class PegasusLevel{
	public static void main(String[] args) throws InvalidMoveException {
		
		Tile[][] map = new Tile[5][5];
		
		for(int i = 0; i<5; i++) {
			for(int j = 0; j<5; j++) {
				map[i][j] = new OpenTile();
			}
		}
		
		map[1][4] = new BlockedTile();
		
		map[2][4].setPiece(new Pegasus(Team.COMPUTER));
		map[0][4].setPiece(new Pegasus(Team.HUMAN));
		map[2][1].setPiece(new Knight(Team.HUMAN));

		
		StrategyGameState s = new StrategyGameState(Team.HUMAN, null, map);
		
		File newFile = new File("./computer_player/test_levels/pegasus.dat");
		try {
			newFile.createNewFile();
			FileOutputStream saveToFile = new FileOutputStream("./computer_player/test_levels/pegasus.dat");
		    ObjectOutputStream outputStream = new ObjectOutputStream(saveToFile);
			outputStream.writeObject(s);
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}