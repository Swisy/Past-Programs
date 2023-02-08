// Your solution goes here 
function playGuessingGame(numToGuess, totalGuesses = 10) {
    let curGuess = 1;
    let message = "Enter a number between 1 and 100.";
    for(x = totalGuesses; x > 0; x--){
        let guess = prompt(message);
        if(guess == null){
            return 0;
        }
        else if(isNaN(guess)){
            message = "Please enter a number.";
            x += 1;
        }
        else if(guess == numToGuess){
            return curGuess;
        } else if (guess < numToGuess){
            message = guess + " is too small. Guess a larger number.";
            curGuess += 1;
        } else {
            message = guess + " is too large. Guess a smaller number.";
            curGuess += 1;
        }

    }
    return 0;
 }