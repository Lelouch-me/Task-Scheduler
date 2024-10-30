$(document).ready(function() {
  function getDefaultMonth(yearsAgo, monthsAgo) {
    var currentDate = new Date();
    var previousDate = new Date();
    previousDate.setFullYear(currentDate.getFullYear() - yearsAgo, currentDate.getMonth() - monthsAgo);
    var defaultMonth = previousDate.toISOString().slice(0, 7);
    return defaultMonth;
  }

function fetchDataAndDrawLineChart(months,lastMonth, url, chartId, chartTitle) {
  $.ajax({
    url: url + '?months=' + months+'&endMonth=' + lastMonth,
    success: function(result) {
      var data = JSON.parse(result);
      var month = data.month;
      var price1 = data.price;
      var price2 = data.price2;
      var name1 = data.name1;
      var name2 = data.name2;

      if (price2) {
        drawEconomicIndicatorChart(chartId, chartTitle, month, price1, price2, name1, name2);
      } else {
        drawEconomicIndicatorChart(chartId, chartTitle, month, price1, null, name1, null);
      }
    }
  });
}


 function drawEconomicIndicatorChart(chartId, title, month, price1, price2, name1, name2) {
  var series = [{
    name: name1,
    data: price1,
    // showInLegend: false,
  }];

  if (price2) {
    series.push({
      name: name2,
      data: price2,
      //showInLegend: false,
    });
  }

  Highcharts.chart(chartId, {
    chart: {
      type: 'line',
    },
    title: {
      text: title
    },
    xAxis: {
      categories: month,
      title: {
        text: 'Months'
      },
      tickPositioner: function() {
        var positions = [];
        var interval;
        if(month.length <= 12){
			interval = Math.ceil(month.length / 12);
		} 
		else if(month.length  <= 36){
			interval = Math.ceil(month.length / 9);
		}
		else if(month.length  <= 48){
			interval = Math.ceil(month.length / 8);
		} 
		else if(month.length  <= 60){
			interval = Math.ceil(month.length / 5);
		}
		else if(month.length  <= 70){
			interval = Math.ceil(month.length / 5);
		}
		else if(month.length  <= 80){
			interval = Math.ceil(month.length / 6);
		}
		else if(month.length  <= 90){
			interval = Math.ceil(month.length / 6);
		} 
		else if(month.length  <= 100){
			interval = Math.ceil(month.length / 7);
		}
		else if(month.length  <= 110){
			interval = Math.ceil(month.length /6);
		}
		else if(month.length  <= 130){
			interval = Math.ceil(month.length / 6);
		}
		else
			interval = Math.ceil(month.length / 3); 
		

        for (var i = 0; i < month.length; i++) {
          if (i === 0 || i === month.length - 1 || i % interval === 0) {
            positions.push(i);
          }
        }
        return positions;
      },
    },
    yAxis: {
      title: {
        text: 'Values'
      },
      plotLines: [{
        value: 0,
        width: 1,
        color: '#808080'
      }]
    },
    tooltip: {
      formatter: function() {
        return '<strong>' + this.x + ': </strong>' + this.y;
      }
    },
    
    plotOptions: {
      line: {
        dataLabels: {
		  allowOverlap:true,
          enabled: true,
          formatter: function() {
            if (this.x === month[month.length - 1]) {
             // return this.y +"<br>" + this.x;
              return "("+this.x +")"+"\n " + this.y.toFixed(2);
            }
            return null;
          }
        }
      }
    },
    series: series
  });
}


  var currentDate = new Date();
  var defaultEndMonth = currentDate.toISOString().slice(0, 7);
  
  var exportDefaultMonth = getDefaultMonth(1, 0); //  year, current month
  var callMoneyDefaultMonth = getDefaultMonth(1, 0); // Last year, previous month
  var foreignExchangeDefaultMonth = getDefaultMonth(6, 0); // Last year, previous month
  var remittanceDefaultMonth = getDefaultMonth(5, 0); // Last year, previous month
  var exchangeDefaultMonth = getDefaultMonth(1, 0); // Last year, previous month
  var inflationDefaultMonth = getDefaultMonth(6, 0); // Last year, previous month
  var lendingDepositDefaultMonth = getDefaultMonth(7, 0); // Last year, previous month

  $('#exportMonths').val(exportDefaultMonth); // Set the default month in the exportMonths input field
  $('#exportEndMonths').val(defaultEndMonth);
  $('#callMoneyMonths').val(callMoneyDefaultMonth); // Set the default month in the callMoneyMonths input field
  $('#callMoneyEndMonths').val(defaultEndMonth);
  $('#foreignExchangeMonths').val(foreignExchangeDefaultMonth); // Set the default month in the callMoneyMonths input field
  $('#foreignExchangeEndMonths').val(defaultEndMonth);
  $('#remittanceMonths').val(remittanceDefaultMonth); // Set the default month in the callMoneyMonths input field
  $('#remittanceEndMonths').val(defaultEndMonth);
  $('#exchangeMonths').val(exchangeDefaultMonth); // Set the default month in the callMoneyMonths input field
  $('#exchangeEndMonths').val(defaultEndMonth);
  $('#inflationMonths').val(inflationDefaultMonth); // Set the default month in the callMoneyMonths input field
  $('#inflationEndMonths').val(defaultEndMonth);
  $('#lendingDepositMonths').val(lendingDepositDefaultMonth); // Set the default month in the callMoneyMonths input field
  $('#lendingDepositEndMonths').val(defaultEndMonth);

  fetchDataAndDrawLineChart($('#exportMonths').val(),  defaultEndMonth,'/economyExportChart', 'export-chart', 'Export Chart (In bn USD)');
  fetchDataAndDrawLineChart($('#inflationMonths').val(), defaultEndMonth,'/inflationChart', 'inflation-chart', 'Rate of Inflation (Base 1995-96=100)');
  fetchDataAndDrawLineChart($('#callMoneyMonths').val(), defaultEndMonth,'/callMoneyRateChart', 'callMoney-chart', 'Call Money Rate (%)');
  fetchDataAndDrawLineChart($('#lendingDepositMonths').val(),defaultEndMonth, '/lendingDepositChart', 'lendingDeposit-chart', 'Lending & Deposit Rates (%)');
  fetchDataAndDrawLineChart($('#remittanceMonths').val(),defaultEndMonth, '/remittanceChart', 'remittance-chart', 'Remittance in bn (USD)');
  fetchDataAndDrawLineChart($('#exchangeMonths').val(),defaultEndMonth, '/exchangeRateChart', 'exchangeRate-chart', 'Exchange Rate (1 USD to BDT)');
  fetchDataAndDrawLineChart($('#foreignExchangeMonths').val(),defaultEndMonth, '/foreignExchangeChart', 'foreignExchange-chart', 'Foreign Exchange Reserve In bn (USD)');
  

  $('#exportForm').submit(function(event) {
    event.preventDefault();
    var months = $('#exportMonths').val();
    var lastMonth=$('#exportEndMonths').val();
    fetchDataAndDrawLineChart(months, lastMonth,'/economyExportChart', 'export-chart', 'Export Chart (In bn USD)');
  });

  $('#callMoneyForm').submit(function(event) {
    event.preventDefault();
    var months = $('#callMoneyMonths').val();
    var lastMonth=$('#callMoneyEndMonths').val();
    fetchDataAndDrawLineChart(months, lastMonth,'/callMoneyRateChart', 'callMoney-chart', 'Call Money Rate (%)');
  });
  
  $('#foreignExchangeForm').submit(function(event) {
    event.preventDefault();
    var months = $('#foreignExchangeMonths').val();
    var lastMonth=$('#foreignExchangeEndMonths').val();
    fetchDataAndDrawLineChart(months, lastMonth,'/foreignExchangeChart', 'foreignExchange-chart', 'Foreign Exchange Reserve In bn (USD)');
  });
  
  $('#remittanceForm').submit(function(event) {
    event.preventDefault();
    var months = $('#remittanceMonths').val();
    var lastMonth=$('#remittanceEndMonths').val();
    fetchDataAndDrawLineChart(months, lastMonth,'/remittanceChart', 'remittance-chart', 'Remittance In bn (USD)');
  });
  
  $('#exchangeForm').submit(function(event) {
    event.preventDefault();
    var months = $('#exchangeMonths').val();
    var lastMonth=$('#exchangeEndMonths').val();
    fetchDataAndDrawLineChart(months, lastMonth,'/exchangeRateChart', 'exchangeRate-chart', 'Exchange Rate (1 USD to BDT)');
  });
  
  $('#inflationForm').submit(function(event) {
    event.preventDefault();
    var months = $('#inflationMonths').val();
    var lastMonth=$('#inflationEndMonths').val();
    fetchDataAndDrawLineChart(months,lastMonth,'/inflationChart', 'inflation-chart', 'Rate of Inflation (Base 1995-96=100)');
  });
  
  $('#lendingDepositForm').submit(function(event) {
    event.preventDefault();
    var months = $('#lendingDepositMonths').val();
    var lastMonth=$('#lendingDepositEndMonths').val();
    fetchDataAndDrawLineChart(months,lastMonth, '/lendingDepositChart', 'lendingDeposit-chart', 'Lending & Deposit Rates (%)');
  });
});
