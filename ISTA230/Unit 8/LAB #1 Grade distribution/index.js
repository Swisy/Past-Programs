function parseScores(scoresString) {
   // TODO: Compete the function
   return scoresString.split(" ");
}

function buildDistributionArray(scoresArray) {
   // TODO: Compete the function
   let grades = [0, 0, 0, 0, 0];
   for(const score of scoresArray){
      if(parseInt(score) >= 90){
         grades[0] += 1;
      } else if(parseInt(score) >= 80){
         grades[1] += 1;
      } else if(parseInt(score) >= 70){
         grades[2] += 1;
      } else if(parseInt(score) >= 60){
         grades[3] += 1;
      } else{
         grades[4] += 1;
      }
   }
   return grades;
}

function setTableContent(userInput) {
   // TODO: Compete the function
   let parsedScores = parseScores(userInput);
   let distributionArray = buildDistributionArray(parsedScores);
   let firstRow = "";
   let thirdRow = "";

   for(x = 0; x < 5; x++){
      temp = '<td><div style="height:';
      temp += (10 * distributionArray[x]);
      temp += 'px" class="bar'
      temp += x;
      temp += '"></div></td>';
      firstRow += temp;
      thirdRow += '<td>'; 
      thirdRow += distributionArray[x];
      thirdRow += '</td>';
   }
   document.getElementById("firstRow").innerHTML = firstRow;
   document.getElementById("thirdRow").innerHTML = thirdRow;
}

// The argument can be changed for testing purposes
setTableContent("45 78 98 83 86 99 90 59");