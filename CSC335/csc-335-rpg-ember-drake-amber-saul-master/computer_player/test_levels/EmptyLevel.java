package computer_player.test_levels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import model.StrategyGameState;
import model.Team;
import onboard.InvalidMoveException;
import onboard.Knight;
import onboard.OpenTile;
import onboard.Tile;

public class EmptyLevel{
	public static void main(String[] args) throws InvalidMoveException {
		
		Tile[][] map = new Tile[5][5];
		
		for(int i = 0; i<5; i++) {
			for(int j = 0; j<5; j++) {
				map[i][j] = new OpenTile();
			}
		}
		
		map[2][2].setPiece(new Knight(Team.COMPUTER));

		
		StrategyGameState s = new StrategyGameState(Team.HUMAN, null, map);
		
		File newFile = new File("./computer_player/test_levels/empty.dat");
		try {
			newFile.createNewFile();
			FileOutputStream saveToFile = new FileOutputStream("./computer_player/test_levels/empty.dat");
		    ObjectOutputStream outputStream = new ObjectOutputStream(saveToFile);
			outputStream.writeObject(s);
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}