function drawAreaChart(time, values, idx) {
        Highcharts.chart('container-area-'+idx, {
            chart : {
                type : 'area',
                styledMode : true
            },
            title : {
                text : ''
            },
            exporting: {
    			enabled: false
  			},
            xAxis : {
            	gridLineWidth: 1,
                title : {
                    text : 'Time'
                },
                categories : time,
                tickInterval: Math.round(time.length/5.4),
                /*
                labels: {
					formatter: function() {
            			if (this.isLast || this.isFirst || (this.value=='11:00') || new String(this.value).valueOf()=='12:00' || (this.value==='13:00') || (this.value==='14:00')) {
              				return this.value
              			}
            		}
				}*/
            },
            yAxis : {
            	gridLineWidth: 1,
                className : 'highcharts-color-0',
                title : {
                    text : ''
                },
				min: Math.min(...values)-10,
				max: Math.max(...values)+5,
            	tickInterval: 15,
            },
            tooltip: {
                pointFormat: idx.toUpperCase() +' had Capital Value <b>{point.y}</b><br/>'
            },
            credits: {
        		enabled: false
    		},
            series : [ {
				showInLegend: false,
                data : values
            } ]
        });
    }
    
function drawBarChart(sector, turnover, type) {        
    Highcharts.chart('container-bar-'+type, {
		chart: {
        type: 'bar',
    	},
    	title : {
                text : ''
            },
            exporting: {
    			enabled: false
  			},
   	 	xAxis: {
        	categories: sector,
        	labels: {
				 step: 1
        	}
    	},
    	
    	yAxis: {
    		title : {
                    text : 'Value (mn)'
                }
    	},

    	plotOptions: {
        	series: {
            	dataLabels: {
                	align: 'left',
                	enabled: true,
                	color: ['#000000'],
                	format: '{y}'
            	},
            	size: '70%',
        	}
    	},

    	colors: ['#31859c'],
		credits: {
        	enabled: false
    	},
    	series: [{
			showInLegend: false,
        	data: turnover
    	}]
	});
}

function drawChartForReturn(sector, returns){
	Highcharts.chart('container-bar-return', {
    chart: {
        type: 'bar'
    },
    title: {
        text: ''
    },
    exporting: {
    			enabled: false
  			},
    xAxis: {
    	gridLineWidth: 1,
      categories: sector
    },
    yAxis: {
    	gridLineWidth: 1,
    	title : {
                    text : 'Return'
                }
    },
    plotOptions: {
        series: {
            dataLabels: {
                align: 'right',
                enabled: true,
                color: ['#000000'],
                format: '{y} %'
            }
        }
    },
    credits: {
        enabled: false
    },
    series: [{
        showInLegend: false,
        data: returns
    }]
});
}
function drawChartForPriceVolume(date, price, volume, id,tickDiff){
	Highcharts.chart(id, {
  		chart: {
    		marginRight: 80
  		},
  		title: {
    		text: ''
 		},
 // 		subtitle: {
 //   		text: 'YTD = '+companyInfo.ytd+'% <br> One year price return =2.55% <br> Divident Yield =1.79% <br> Total one year return= 4.34%'
 // 		},
 		credits: {
        	enabled: false
    	},
  		xAxis: {
   			categories: date,
   			tickInterval: tickDiff
  		},
  		yAxis: [{
    		title: {
      			text: 'Price'
    		}
  		}, 
  		{
    		title: {
      			text: 'Volume (mn)'
    		},
    		opposite: true
  		}],
		plotOptions: {
    			series: {
      				turboThreshold: 0 // Comment out this code to display error
    			}
  			},
  		series: [{
    		type: 'line',
    		data: price,
    		name: 'Closing Price'
  			}, {
    		type: 'column',
    		data: volume,
    		name: 'Volume',
    		yAxis: 1
  		}]
	});
}

function drawChartForShareHolder(shares){
	// Create the chart
	Highcharts.chart('shareholding', {
	  chart: {
	    type: 'pie',
	    marginRight: 0,
	    marginLeft: 0,
	  },
	  title: {
	      text: ''
	    },
	    tooltip: {
	      pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	    },
	    accessibility: {
	      point: {
	        valueSuffix: '%'
	      }
	    },
	    plotOptions: {
	      pie: {
	        allowPointSelect: true,
	        cursor: 'pointer',
	        dataLabels: {
	          enabled: true,
	          format: '<b>{point.name}</b>: {point.percentage:.1f} %'
	        }
	      }
	    },
	    credits: {
	        enabled: false
	    },
	  series: [{
	    name: 'Share',
	    colorByPoint: true,
	    data: [{
	        name: 'Sponsor/Director',
	        y: shares.sponsorDirector,        
	        selected: true
	      }, {
	        name: 'Government',
	        y: shares.govt
	      }, {
	        name: 'Institute',
	        y: shares.institute
	      }, {
	        name: 'Foreign',
	        y: shares.foreign
	      }, {
	        name: 'Public',
	        y: shares.publicc
	      }],
	    size: '85%', 
	    showInLegend: true,
	    dataLabels: {
	      enabled: false
	    }
	  }],
	  legend: {
	    layout: "horizontal",
	    align: "center",
	    verticalAlign: "bottom",
	  }
	});

}
function drawTreeChartForSectorTurnover(sector, turnover){
	Highcharts.chart('treemap-container', {
            colorAxis: {
                minColor: '#FFFFFF',
            },
            credits: {
	        	enabled: false
	    	},
            series: [{
                type: 'treemap',
                layoutAlgorithm: 'squarified',
                data: [{
                    name: sector[0],
                    value: turnover[0],
                    color: '#3fb0ea',
                }, {
                    name: sector[1],
                    value: turnover[1],
                    color: '#005aa6',
                }, {
                    name: sector[2],
                    value: turnover[2],
                    color: '#835301',
                }, {
                    name: sector[3],
                    value: turnover[3],
                    color: '#0a8647',
                }, {
                    name: sector[4],
                    value: turnover[4],
                    color: '#54515a',
                }, {
                    name: sector[5],
                    value: turnover[5],
                    color: '#a93701',
                }, {
                    name: sector[6],
                    value: turnover[6],
                    color: '#014694',
                }, {
                    name: sector[7],
                    value: turnover[7],
                    color: '#03a447',
                }, {
                    name: sector[8],
                    value: turnover[8],
                    color: '#009ccc',
                }, {
                    name: sector[9],
                    value: turnover[9],
                    color: '#eeb100',
                }, {
                    name: sector[10],
                    value: turnover[10],
                    color: '#95a49f',
                }, {
                    name: sector[11],
                    value: turnover[11],
                    color: '#e6721d',
                }, {
                    name: sector[12],
                    value: turnover[12],
                    color: '#82E0AA',
                }, {
                    name: sector[13],
                    value: turnover[13],
                    color: '#73C6B6',
                }, {
                    name: sector[14],
                    value: turnover[14],
                    color: '#B2BABB',
                }, {
                    name: sector[15],
                    value: turnover[15],
                    color: '#BB8FCE',
                }, {
                    name: sector[16],
                    value: turnover[16],
                    color: '#F1948A',
                }, {
                    name: sector[17],
                    value: turnover[17],
                    color: '#F8C471',
                }, {
                    name: sector[18],
                    value: turnover[18],
                    color: '#9EDE00',
                },]
            }],
            title: {
                text: ''
            },
            exporting: {
    			enabled: false
  			},
            dataLabels: {
                align: 'left',
                verticalAlign: 'bottom',
                useHTML: true,
                allowOverlap: true,
                crop: false,
                overflow: 'allow',
            },
        });
}

Highcharts.getJSON("/api/chartData/sectorValue", function(dataJson){drawFullTreeMap(dataJson,'Value')});
Highcharts.getJSON("/api/chartData/sectorVolume", function(dataJson){drawFullTreeMap(dataJson,'Volume')});
Highcharts.getJSON("/api/chartData/sectorMarketCap", function(dataJson){drawFullTreeMap(dataJson,'Mcap')});

function drawFullTreeMap(dataJson,type) {
    Highcharts.chart("container-full-tree-map-"+type, {
      accessibility: {
        screenReaderSection: {
          beforeChartFormat: '<{headingTagName}>{chartTitle}</{headingTagName}><div>{typeDescription}</div><div>{chartSubtitle}</div><div>{chartLongdesc}</div>',
        },
      },
//      colors:colors,
      credits: {
	      enabled: false
	  },
	  chart: {
    	events: {
      		redraw: function() {
        		if (this.series[0].rootNode) {
          			$('.highcharts-data-label-color-0').attr('display', 'none');
        		} else {
          			$('.highcharts-data-label-color-0').attr('display', 'initial');
        		}
      		}
    	}
  	},
      series: [
        {
          name: 'DSE',
          type: "treemap",
          layoutAlgorithm: "squarified",
          allowDrillToNode: true,
          alternateStartingDirection: true,
    	  levelIsConstant: false,
          dataLabels: {
            enabled: false,
            crop:true
          },
          borderColor: "black",
          levels: [{
              level: 1,
//              colorByPoint:true,
              dataLabels: {
                enabled: true,
                style: {
                  textOutline: false,
                  fontSize: 16,
                },
              },
              borderWidth: 3
          }, {
      	  level: 2,
      	  dataLabels: {
                enabled: false,
                style: {
                  textOutline: true,
                  fontSize: 10,
                },
              },
          borderWidth: 0
    		}
          ],
          data: dataJson
        }
      ],
      exporting: {
			    buttons: {
			        contextButton: {
			            menuItems: [
							'viewFullscreen',
			                'separator',
			                'downloadPNG',
			                'downloadJPEG',
			                'downloadPDF',
			                'separator',
			                'printChart',
			            ]
			        }
			    }
			},
      title: {
        text: ""
      },
      tooltip: {
        useHTML: true,
        valueDecimals: 2,
        pointFormat: "<b>{point.name}</b> <br>"+type+": {point.value} mn (BDT)"+
        				"<br>{point.ltp} <br>{point.ret} <br>{point.trade}"
      }
    });
}

function drawPieChartForMyFinance(shares){
    		// Create the chart
    		Highcharts.chart('invest_plan_pie_chart', {
    		  chart: {
    		    type: 'pie',
    		    marginRight: 0,
    		    marginLeft: 0,
    		    marginTop: 0
    		  },
    		  title: {
    		      text: ''
    		    },
    		    tooltip: {
    		      pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
    		    },
    		    accessibility: {
    		      point: {
    		        valueSuffix: '%'
    		      }
    		    },
    		    plotOptions: {
    		      pie: {
    		        allowPointSelect: true,
    		        size: '100%',
    		        cursor: 'pointer',
    		        dataLabels: {
    		          enabled: false,
    		          //format: '<b>{point.name}</b>: {point.percentage:.1f} %'
    		        },
    		        showInLegend: true,
    		      }
    		    },
    		    credits: {
    		        enabled: false
    		    },
    		    series: [{
    			    name: 'Share',
    			    colorByPoint: true,
    			    data: shares,
    			    //size: '95%', 
    			    /*dataLabels: {
    			      enabled: false
    			    }*/
    			  }],
    		  /*legend: {
    		    layout: "horizontal",
    		    align: "center",
    		    verticalAlign: "bottom",
    		  },*/
    		  exporting: {
  		        enabled: false,
  		  	  }
    		});

}

function drawSimulationChart(returnArray, age, ret_age_point) {
    		Highcharts.chart('asset_value', {
    	            chart: {
    	            },
    	            title: {
    	                text: ''
    	            },
    	            tooltip: {
    	                //pointFormat: '<span style="color:{series.color};">\u25CF</span> {series.name} : <b>{point.y} mn</b>',
    	                pointFormatter: function() { 
    	                    return '<span style="color:'+this.series.color+';">\u25CF</span><b> ' +this.series.name + '</b> : <b>' + (this.y/1000000).toFixed(2)+' mn BDT'; 
    	                  }
    	            },
    	            xAxis: {
    	                categories : age,
    	                //tickInterval: tickDiff,
    	                title: {
    	                    text: 'Investor Age',
    	                },
    	                plotLines: [{
    	                    width: 2,
    	                    value: ret_age_point
    	                }]
    	            },
    	            yAxis: [{
    	                title: {
    	                    text: ''
    	                }
    	            }],
    				credits: {
    	        		enabled: false
    	    		},
    	            series: [{
    	                type: 'line',
    	                data: returnArray[0],
    	                name: '+3SD'
    	            }, {
    	                type: 'line',
    	                data: returnArray[1],
    	                name: '+2SD'
    	            }, {
    	                type: 'line',
    	                data: returnArray[2],
    	                name: '+SD'
    	            }, {
    	                type: 'line',
    	                data: returnArray[3],
    	                name: 'MEAN'
    	            }, {
    	                type: 'line',
    	                data: returnArray[4],
    	                name: '-SD'
    	            }, {
    	                type: 'line',
    	                data: returnArray[5],
    	                name: '-2SD'
    	            }, {
    	                type: 'line',
    	                data: returnArray[6],
    	                name: '-3SD'
    	            }],
    	            exporting: {
    				    buttons: {
    				        contextButton: {
    				            menuItems: [
    								'viewFullscreen',
    				                'separator',
    				                'downloadPNG',
    				                'downloadJPEG',
    				                'downloadPDF',
    				                'separator',
    				                'downloadCSV',
    				                'downloadXLS',
    				                'printChart',
    				            ]
    				        }
    				    }
    				}
    	        });    	        
}


$(document).ready(function() {
  var currentDate = new Date();
  var defaultMonths = [];
  var chartData = [];

  // Generate default months (last 6 months)
  for (var i = 6; i >= 1; i--) {
    var previousDate = new Date();
    previousDate.setMonth(currentDate.getMonth() - i);
    var month = previousDate.toLocaleString('default', { month: 'short' }) + '-' + previousDate.getFullYear().toString().substr(-2);
    defaultMonths.push(month);
  }

  // Retrieve chart data for the default months
  defaultMonths.forEach(function(month) {
    $.ajax({
      url: '/yieldChart?months=' + month,
      success: function(result) {
        var price = JSON.parse(result).price;
        var chart = {
          category: month,
          serie1: price
        };
        chartData.push(chart);

        if (chartData.length === defaultMonths.length) {
          yieldCharts(defaultMonths, chartData);
        }
      },
    });
  });
});

function generateIndicatorChart() {
	var currentDate = new Date();
	var defaultMonths = [];
  	var chartData = [];
  	
  	// Generate default months (last 6 months)
  for (var i = 6; i >= 1; i--) {
    var previousDate = new Date();
    previousDate.setMonth(currentDate.getMonth() - i);
    var month = previousDate.toLocaleString('default', { month: 'short' }) + '-' + previousDate.getFullYear().toString().substr(-2);
    defaultMonths.push(month);
  }
	// Event listener for form submission
  var selectedMonth = document.getElementById("yieldMonths").value;
  if (selectedMonth) {
    var year = selectedMonth.slice(2);
    var month = new Date(selectedMonth).toLocaleString('default', { month: 'short' });

    // Format the month and year in MMM-yy format
    var formatted = month + '-' + year;   
    var parts = formatted.split('-');
	var formattedMonth = parts[0] + '-' + parts[1].slice(-2);
	defaultMonths.push(formattedMonth);

    // Retrieve chart data for the selected month
 defaultMonths.forEach(function(month) {
    $.ajax({
      url: '/yieldChart?months=' + month,
      success: function(result) {
        var price = JSON.parse(result).price;
        var chart = {
          category: month,
          serie1: price
        };
        chartData.push(chart);

        if (chartData.length === defaultMonths.length) {
          yieldCharts(defaultMonths, chartData);
        }
      },
    });
  });
  }
}

function yieldCharts(months, charts) {
  Highcharts.chart('yield-chart', {
    chart: {
      type: 'line',
      //width: 500
    },
    
    title: {
      text: 'Yield'
    },
  
    xAxis: {
      categories: ['91-Day T-bill', '182-Day T-bill', '364-Day T-bill', '5 yr T-bond', '10 yr T-bond', '15 yr T-bond', '20 yr T-bond'],
      title: {}
    },
    
    legend: {
      layout: 'vertical',
      align: 'right',
      verticalAlign: 'middle'
    },
    
    yAxis: {
      title: {
        text: "Value"
      },
      plotLines: [{
        value: 0,
        width: 1,
        color: '#808080'
      }]
    },
    
    tooltip: {
      formatter: function() {
        return '<strong>'+this.x+': </strong>'+ this.y;
      }
    },
  
    series: charts.map(function(chart, index) {
      return {
        data: chart.serie1,
        name: months[index],
        showInLegend: true
      };
    })
  });
}