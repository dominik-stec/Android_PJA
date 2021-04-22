//<!--https://canvasjs.com/-->


window.onload = function () {
  var dataPoints = [], currentDate = new Date(), rangeChangedTriggered = false;
  var stockChart = new CanvasJS.StockChart("chartContainer",{
    theme: "dark1", //"light2", "dark1", "dark2"
    title:{
      text:"Wystąpienia zachorowań z powodu otyłości"
    },
    rangeChanged: function(e) {
        rangeChangedTriggered = true;
    },
    charts: [{
      axisX: {
        crosshair: {
          enabled: true,
          valueFormatString: "MMM DD, YYYY HH:mm:ss"
        }
      },
      axisY: {
        title: "Zachorowania na świecie w ciągu sekundy"
      },
      toolTip: {
        shared: true
      },
      data: [{
        type: "line",
        name: "Zachorowania",
        xValueFormatString: "MMM DD, YYYY HH:mm:ss",
        xValueType: "dateTime",
        dataPoints : dataPoints
      }]
    }],
    navigator: {
      slider: {
        minimum: new Date(currentDate.getTime() - (90 * 1000))
      },
      axisX: {
        labelFontColor: "white"
      }
    },
    rangeSelector: {
      enabled: false
    }
  });
  var dataCount = 700, ystart = 50, interval = 1000, xstart = (currentDate.getTime() - (700 * 1000));
  updateChart(xstart, ystart, dataCount, interval);
  function updateChart(xstart, ystart, length, interval) {
    var xVal = xstart, yVal = ystart;
    for(var i = 0; i < length; i++) {
      yVal = yVal +  Math.round(5 + Math.random() *(-5-5));
      yVal = Math.min(Math.max(yVal, 5), 90);
      dataPoints.push({x: xVal,y: yVal});
      xVal += interval;
    }
    if(!rangeChangedTriggered) {
        stockChart.options.navigator.slider.minimum = new Date(xVal - (90 * 1000)) ;
    }
    xstart = xVal;
    dataCount = 1;
    ystart = yVal;
    stockChart.render();
    setTimeout(function() { updateChart(xstart, ystart, dataCount, interval); }, 1000);
  }
}