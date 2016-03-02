
public class minimax_998796497_912054270 extends AIModule{
	private int depthmax; 
	private int MaxMove; 
	private int[] loadfactor = {3, 2, 4, 1, 5, 0, 6}; 
	private EvaluationTable currET = new EvaluationTable(); 
	
	public void getNextMove(final GameStateModule game)
	{
		currET.opponentsMove(game); 
		depthmax = 0; 

		while(!terminate)
		{
			miniMax(game, currET, 0, 1); 
		
			if(!terminate)
			{
				chosenMove = MaxMove; 
			}
			
			depthmax++; //increment max depth for next iterations. 
		}
		// update the move 
		currET.getMove(1, chosenMove);
	}
	
	private int miniMax(final GameStateModule game, EvaluationTable MT, int depth, int playerID)
	{
		int v = 0; 
		if(terminate)
			return 0; 
		if(depth == depthmax)
		{
			return MT.EvaluationFunction();// return by finding the score. 
		}
			
		depth++; 
		EvaluationTable deepcopy;
		if(playerID == 1)
		{
			// This is our max value 
			int max = Integer.MIN_VALUE; 
			for(int i:loadfactor) 
			{
				if(game.canMakeMove(i))
				{
					game.makeMove(i);	
					deepcopy = new EvaluationTable(MT);
					deepcopy.getMove(1, i);
					
					v = miniMax(game, deepcopy, depth, 2); 
					
					if(max < v)
					{
						max = v; 
						if(depth == 1)
							MaxMove = i;
					}
					game.unMakeMove();
				}
			}
			return max; 
		}
		
		else
		{
			// This is our min value 
			int min = Integer.MAX_VALUE; 
			for(int i: loadfactor)
			{
				if(game.canMakeMove(i))
				{
					game.makeMove(i);
					deepcopy = new EvaluationTable(MT);
					deepcopy.getMove(2, i);
					v = miniMax(game, deepcopy, depth, 1);
					if(min > v)
						min = v; 
					game.unMakeMove();
					
				}
			}
			return min; 
		}
	}
	
	/*private void printBoard(final GameStateModule state)
	{
		for(int i = 5; i >= 0; i++)
		{
			for(int j = 0; j < 7; j++)
			{
				if(state.getAt(j, i)==1)
					System.out.print(" O");
				else if(state.getAt(j, i) == 2)
					System.out.print(" X");
				else 
					System.out.print(" .");
			}
			System.out.println();
		}
		System.out.println();
	}*/
	
	public class EvaluationTable{
		private int[][] x; 
		private int[][] y; 
		private int[][] dx; 
		private int[][] dy;
	
		public EvaluationTable()
		{
			x = new int[8][9]; 
			y = new int[8][9];
			dx = new int[8][9];
			dy = new int[8][9];
		}
		
		public EvaluationTable(EvaluationTable T)
		{
			x = new int[8][9]; 
			y = new int[8][9];
			dx = new int[8][9];
			dy = new int[8][9];
			
			for(int i = 0; i < 8; i++)
			{
				for(int j = 0; j < 9; j++)
				{
					this.x[i][j] = T.x[i][j]; 
					this.y[i][j] = T.y[i][j]; 
					this.dx[i][j] = T.dx[i][j]; 
					this.dy[i][j] = T.dy[i][j]; 
				}
			}
		}
		public int EvaluationFunction() 
		{
			int score = 0; 
			int[][] weights = 
				{{1, 1, 1, 2, 1, 1, 1},	
	             {1, 2, 2, 3, 2, 2, 1},
	             {1, 2, 3, 4, 3, 2, 1},
	             {1, 2, 3, 4, 3, 2, 1},
	             {1, 2, 2, 3, 2, 2, 1},		  				  		
	             {1, 1, 1, 2, 1, 1, 1}};
			
			for(int i = 1; i <= 6; i++)
			{
				for(int j = 1; j <= 7; j++)
				{
					if(x[i][j]==4 || y[i][j] == 4 || dx[i][j] == 4 || dy[i][j]==4)
						score = score + 1000; 
					else if (x[i][j]== -4 || y[i][j] == -4 || dx[i][j] == -4 || dy[i][j]==-4)
						score = score - 1000; 
					
					score += (x[i][j] + y[i][j] + dx[i][j] + dy[i][j])*(weights[i-1][j-1]);
				}
			}
			return score; 		
		}
		
		public void opponentsMove(final GameStateModule state)
		{
			for(int i=0; i<6; i++) {
	            for(int j=0; j<7; j++) {
	                if( state.getAt(j,i) != 0 && x[i+1][j+1] == 0 ) 
	                {
	                    getMove(2, j);
	                    return; 
	                }
	            }
	        }
		}
		
		public void getMove( int player, int col)
		{
			int t = 0; 
			
			if(player == 1)
				t = 1; 
			else 
				t = -1; 
			col++; //adjust columns in the array 
			int row = 1; //adjust temporary row. 
			while(x[row][col] != 0)
				row++; 
			
			if(col > 7 || col < 1 || row > 6 || row < 1)
			{
				System.out.println("Out of bounds column:" + col + " row:" + row);
				System.out.println();
				return; 
			}
					
			//updated vertical 
			int a = t*x[row-1][col]; 
			
			if(a > 0)
				for(int i = 0; i <= a; i++)
					x[row-i][col] = a*t + t; 			
			else
				x[row][col] = t; 
			
			//update horizontal 
			int l = t*y[row][col-1];
			int r = t*y[row][col+1];
			
			if(l > 0 && r > 0) // check both sides
				for(int i = 0; i<= r+l; i++)
					y[row][col-l+i] = t*(l+r) + t; 
			else if(l > 0)// check left
				for(int i = 0; i <= l; i++)
					y[row][col-i] = t*l + t; 
			else if(r > 0) 
				for(int i = 0; i <= r; i++)
					y[row][col+i] = t*r + t; 
			else // niether sides
				y[row][col] = t; 
			
			// update diagonal positive 
			int tr = t*dx[row+1][col+1]; 
			int bl = t*dx[row-1][col-1]; 
			
			if(tr > 0 && bl > 0)
				for(int i = 0; i <= tr+bl; i++)
					dx[row-bl+i][col-bl+i] = t*(bl+tr)+t;
			else if (tr > 0)
				for(int i = 0; i <= tr; i++)
					dx[row+i][col+i] = t*tr+t;
			else if (bl > 0)
				for(int i = 0; i <= bl; i++)
					dx[row-i][col-i] = t*bl+t;
			else // niether sides
				dx[row][col] = t;
			
			//update diagonal negative 
			int tl = t*dy[row+1][col-1]; 
			int br = t*dy[row-1][col+1]; 
			
			if(tl > 0 && br > 0)
				for(int i = 0; i<= tl+br; i++)
					dy[row-br+i][col+br-i] = t*(br+tl)+t;
			else if (tl > 0)
				for(int i = 0; i <= tl; i++)
					dy[row+i][col-i] = t*tl+t;
			else if (br > 0)
				for(int i = 0; i <= br; i++)
					dy[row-i][col+i] = t*br+t;
			else // niether sides
				dy[row][col] = t;
		}	
	}	
}
