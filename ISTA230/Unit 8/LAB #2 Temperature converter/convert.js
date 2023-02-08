window.addEventListener("DOMContentLoaded", domLoaded);

function domLoaded() {
   document.getElementById("convertButton").addEventListener("click", converter);
   document.getElementById("cInput").addEventListener("input", fClear);
   document.getElementById("fInput").addEventListener("input", cClear);
}

function converter(){
   if(document.getElementById("cInput").value !== ""){
      if(isNaN(parseFloat(document.getElementById("cInput").value))){
         document.getElementById("errorMessage").innerHTML = document.getElementById("cInput").value + " is not a number";
      } else{
         document.getElementById("fInput").value = convertCtoF(parseFloat(document.getElementById("cInput").value));
         document.getElementById("errorMessage").innerHTML = "";
      }
   } else if(document.getElementById("fInput").value !== ""){
      if(isNaN(parseFloat(document.getElementById("fInput").value))){
         document.getElementById("errorMessage").innerHTML = document.getElementById("fInput").value + " is not a number";
      } else{
         document.getElementById("cInput").value = convertFtoC(parseFloat(document.getElementById("fInput").value));
         document.getElementById("errorMessage").innerHTML = "";
      }
   }

   if(document.getElementById("fInput").value !== ""){
      temp = parseFloat(document.getElementById("fInput").value);

      if(temp < 32){
         document.getElementById("weatherImage").src = "cold.png";
      } else if(temp <= 50){
         document.getElementById("weatherImage").src = "cool.png";
      } else {
         document.getElementById("weatherImage").src = "warm.png";
      }
   }
}

function fClear(){
   document.getElementById("fInput").value = "";
}

function cClear(){
   document.getElementById("cInput").value = "";
}

function convertCtoF(degreesCelsius) {
   // TODO: Complete the function
   return degreesCelsius * 9 / 5 + 32;
}

function convertFtoC(degreesFahrenheit) {
   // TODO: Complete the function
   return ((degreesFahrenheit - 32) * 5 / 9);
}
