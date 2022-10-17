Requirements:
  
As part of your submission, you must provide:

  • At least five classes, Reversi, ReversiView, ReversiController, ReversiModel, and ReversiBoard.
    The Reversi class will have main, separating it from the view. You may have as many additional classes as you need.
  • Complete javadoc for every class and method, using the @author, @param, @return, and
    @throws javadoc tags. Generate your documentation into a doc folder.
    
  • A complete UML diagram for your design, drawn using http://draw.io and the xml file
    committed as part of your repository
    
  • Test cases for your model and controller with 100% branch coverage
  
  • A main class, that launches your view using :
     Application.launch(ReversiView.class, args);
     
  • A ReversiView class that uses JavaFX to display the GUI described above and is an Observer
  
  • A ReversiModel class that extends Observable and notifies its observers when the model
    changes (a move is made)
    
  • A ReversiBoard class that encapsulates the board from the model to pass to the view and is
    Serializable
    
  • A menu for starting a new game
  
  • The ability to save the game when an in-progress window is exited
  
  • The automatic loading of a saved game on the next run
  
  • At least two feature branches, and a commit history that shows each team member’s
    contributions.
    
  • A master branch that contains your final submission
  
  • As always, reasonable comments and style should be followed, with test cases to convince
    yourself that your modifications work

Your code must follow the MVC architecture as we have described it in class. That means:

  • No input or output code except in the View
  
  • A model that represents the state of the game but guards access through public methods
  
  • A controller that allows the view to interact indirectly with the model, providing the abstracted
    operations of your game
    
       o Including a humanTurn(int row, int col) method and a computerTurn()
        method that represent the turns
        
       o A set of methods that determine the end of game and the winner
      
       o Some way to access the board to be able to display it as part of the view
      
       o As few public methods as possible, with helper methods being private and all non-final
        fields being private
        
  • Your UML diagram should be used to plan out the program and will be a Section Lab grade taken
    as part of your final submission
