// Put your solution here
function divideArray(numbers) {
    let evenNums = [];
    let oddNums = [];

    for(number in numbers){
        if(numbers[number] % 2 == 1){
            oddNums.push(numbers[number]);
        } else {
            evenNums.push(numbers[number]);
        }
    }

    evenNums.sort(function(a, b){return a - b});
    oddNums.sort(function(a, b){return a - b});

    console.log("Even numbers:");
    if(evenNums.length > 0){
        for(evenNum in evenNums){
            console.log(evenNums[evenNum]);
        }
    } else{
        console.log("None")
    }

    console.log("Odd numbers:");
    if(oddNums.length > 0){
        for(oddNum in oddNums){
            console.log(oddNums[oddNum]);
        }
    } else{
        console.log("None")
    }

 }