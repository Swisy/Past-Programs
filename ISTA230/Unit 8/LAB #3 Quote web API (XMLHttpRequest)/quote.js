window.addEventListener("DOMContentLoaded", function () {
   document.querySelector("#fetchQuotesBtn").addEventListener("click", function () {

      // Get values from drop-downs
      const topicDropdown = document.querySelector("#topicSelection");
      const selectedTopic = topicDropdown.options[topicDropdown.selectedIndex].value;
      const countDropdown = document.querySelector("#countSelection");
      const selectedCount = countDropdown.options[countDropdown.selectedIndex].value;
   
      // Get and display quotes
      fetchQuotes(selectedTopic, selectedCount);	   
   });
});

function fetchQuotes(topic, count) {
   // TODO: Modify to use XMLHttpRequest
   let endpoint = "https://wp.zybooks.com/quotes.php";
   let queryString = "topic=" + topic + "&count=" + count;
   let url = endpoint + "?" + queryString;

   let xhr = new XMLHttpRequest();
   xhr.addEventListener("load", responseReceivedHandler);
   xhr.responseType = "json";
   xhr.open("GET", url);
   xhr.send();
}

// TODO: Add responseReceivedHandler() here

function responseReceivedHandler() {
   let quotes = document.getElementById("quotes");
   if (this.response.hasOwnProperty('error')) {
      quotes.innerHTML = this.response.error;
   } else {
      temp = "<ol>";

      for(x = 0; x < this.response.length; x++){
         temp += "<li>" + this.response[x].quote + " - " + this.response[x].source + "</li>";
      }

      temp += "</ol>";
      quotes.innerHTML = temp;
   }
}