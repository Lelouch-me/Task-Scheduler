function drawPriceSeriesChartForOne(returnArray, tickers, dateDtls,tickDiff) {
	Highcharts.chart('time-series', {
            chart: {
                //marginRight: 80
            },
            title: {
                text: ''
            },
            xAxis: {
                categories : dateDtls,
                tickInterval: tickDiff
            },
            yAxis: [{
                title: {
                    text: ''
                }
            }],
			credits: {
        		enabled: false
    		},
    		plotOptions: {
    			series: {
      				turboThreshold: 0 // Comment out this code to display error
    			}
  			},
            series: [{
                type: 'line',
                data: returnArray[0],
                name: tickers[0]
            },],
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

function drawPriceSeriesChartForTwo(returnArray, tickers, dateDtls,tickDiff) {
	Highcharts.chart('time-series', {
            chart: {
                //marginRight: 80
            },
            title: {
                text: ''
            },
            xAxis: {
                categories : dateDtls,
                tickInterval: tickDiff
            },
            yAxis: [{
                title: {
                    text: ''
                }
            }],
			credits: {
        		enabled: false
    		},
    		plotOptions: {
    			series: {
      				turboThreshold: 0 // Comment out this code to display error
    			}
  			},
            series: [{
                type: 'line',
                data: returnArray[0],
                name: tickers[0]
            }, {
                type: 'line',
                data: returnArray[1],
                name: tickers[1]
            },],
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

function drawPriceSeriesChartForThree(returnArray, tickers, dateDtls,tickDiff) {
	Highcharts.chart('time-series', {
            chart: {
                //marginRight: 80
            },
            title: {
                text: ''
            },
            xAxis: {
                categories : dateDtls,
                tickInterval: tickDiff
            },
            yAxis: [{
                title: {
                    text: ''
                }
            }],
			credits: {
        		enabled: false
    		},
    		plotOptions: {
    			series: {
      				turboThreshold: 0 // Comment out this code to display error
    			}
  			},
            series: [{
                type: 'line',
                data: returnArray[0],
                name: tickers[0]
            }, {
                type: 'line',
                data: returnArray[1],
                name: tickers[1]
            }, {
                type: 'line',
                data: returnArray[2],
                name: tickers[2]
            },],
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

function drawPriceSeriesChartForFour(returnArray, tickers, dateDtls,tickDiff) {
	Highcharts.chart('time-series', {
            chart: {
                //marginRight: 80
            },
            title: {
                text: ''
            },
            xAxis: {
                categories : dateDtls,
                tickInterval: tickDiff
            },
            yAxis: [{
                title: {
                    text: ''
                }
            }],
			credits: {
        		enabled: false
    		},
    		plotOptions: {
    			series: {
      				turboThreshold: 0 // Comment out this code to display error
    			}
  			},
            series: [{
                type: 'line',
                data: returnArray[0],
                name: tickers[0]
            }, {
                type: 'line',
                data: returnArray[1],
                name: tickers[1]
            }, {
                type: 'line',
                data: returnArray[2],
                name: tickers[2]
            }, {
                type: 'line',
                data: returnArray[3],
                name: tickers[3]
            },],
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

function drawPriceSeriesChartForFive(returnArray, tickers, dateDtls,tickDiff) {
	Highcharts.chart('time-series', {
            chart: {
                //marginRight: 80
            },
            title: {
                text: ''
            },
            xAxis: {
                categories : dateDtls,
                tickInterval: tickDiff
            },
            yAxis: [{
                title: {
                    text: ''
                }
            }],
			credits: {
        		enabled: false
    		},
    		plotOptions: {
    			series: {
      				turboThreshold: 0 // Comment out this code to display error
    			}
  			},
            series: [{
                type: 'line',
                data: returnArray[0],
                name: tickers[0]
            }, {
                type: 'line',
                data: returnArray[1],
                name: tickers[1]
            }, {
                type: 'line',
                data: returnArray[2],
                name: tickers[2]
            }, {
                type: 'line',
                data: returnArray[3],
                name: tickers[3]
            }, {
                type: 'line',
                data: returnArray[4],
                name: tickers[4]
            },],
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

function drawPriceSeriesChartForSix(returnArray, tickers, dateDtls,tickDiff) {
	Highcharts.chart('time-series', {
            chart: {
                //marginRight: 80
            },
            title: {
                text: ''
            },
            xAxis: {
                categories : dateDtls,
                tickInterval: tickDiff
            },
            yAxis: [{
                title: {
                    text: ''
                }
            }],
			credits: {
        		enabled: false
    		},
    		plotOptions: {
    			series: {
      				turboThreshold: 0 // Comment out this code to display error
    			}
  			},
            series: [{
                type: 'line',
                data: returnArray[0],
                name: tickers[0]
            }, {
                type: 'line',
                data: returnArray[1],
                name: tickers[1]
            }, {
                type: 'line',
                data: returnArray[2],
                name: tickers[2]
            }, {
                type: 'line',
                data: returnArray[3],
                name: tickers[3]
            }, {
                type: 'line',
                data: returnArray[4],
                name: tickers[4]
            },{
                type: 'line',
                data: returnArray[5],
                name: tickers[5]
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

function drawPriceSeriesChartForSeven(returnArray, tickers, dateDtls,tickDiff) {
	Highcharts.chart('time-series', {
            chart: {
                //marginRight: 80
            },
            title: {
                text: ''
            },
            xAxis: {
                categories : dateDtls,
                tickInterval: tickDiff
            },
            yAxis: [{
                title: {
                    text: ''
                }
            }],
			credits: {
        		enabled: false
    		},
    		plotOptions: {
    			series: {
      				turboThreshold: 0 // Comment out this code to display error
    			}
  			},
            series: [{
                type: 'line',
                data: returnArray[0],
                name: tickers[0]
            }, {
                type: 'line',
                data: returnArray[1],
                name: tickers[1]
            }, {
                type: 'line',
                data: returnArray[2],
                name: tickers[2]
            }, {
                type: 'line',
                data: returnArray[3],
                name: tickers[3]
            }, {
                type: 'line',
                data: returnArray[4],
                name: tickers[4]
            },{
                type: 'line',
                data: returnArray[5],
                name: tickers[5]
            },{
                type: 'line',
                data: returnArray[6],
                name: tickers[6]
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

function drawPriceSeriesChartForEight(returnArray, tickers, dateDtls,tickDiff) {
	Highcharts.chart('time-series', {
            chart: {
                //marginRight: 80
            },
            title: {
                text: ''
            },
            xAxis: {
                categories : dateDtls,
                tickInterval: tickDiff
            },
            yAxis: [{
                title: {
                    text: ''
                }
            }],
			credits: {
        		enabled: false
    		},
    		plotOptions: {
    			series: {
      				turboThreshold: 0 // Comment out this code to display error
    			}
  			},
            series: [{
                type: 'line',
                data: returnArray[0],
                name: tickers[0]
            }, {
                type: 'line',
                data: returnArray[1],
                name: tickers[1]
            }, {
                type: 'line',
                data: returnArray[2],
                name: tickers[2]
            }, {
                type: 'line',
                data: returnArray[3],
                name: tickers[3]
            }, {
                type: 'line',
                data: returnArray[4],
                name: tickers[4]
            },{
                type: 'line',
                data: returnArray[5],
                name: tickers[5]
            },{
                type: 'line',
                data: returnArray[6],
                name: tickers[6]
            },{
                type: 'line',
                data: returnArray[7],
                name: tickers[7]
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

function drawPriceSeriesChartForNine(returnArray, tickers, dateDtls,tickDiff) {
	Highcharts.chart('time-series', {
            chart: {
                //marginRight: 80
            },
            title: {
                text: ''
            },
            xAxis: {
                categories : dateDtls,
                tickInterval: tickDiff
            },
            yAxis: [{
                title: {
                    text: ''
                }
            }],
			credits: {
        		enabled: false
    		},
    		plotOptions: {
    			series: {
      				turboThreshold: 0 // Comment out this code to display error
    			}
  			},
            series: [{
                type: 'line',
                data: returnArray[0],
                name: tickers[0]
            }, {
                type: 'line',
                data: returnArray[1],
                name: tickers[1]
            }, {
                type: 'line',
                data: returnArray[2],
                name: tickers[2]
            }, {
                type: 'line',
                data: returnArray[3],
                name: tickers[3]
            }, {
                type: 'line',
                data: returnArray[4],
                name: tickers[4]
            },{
                type: 'line',
                data: returnArray[5],
                name: tickers[5]
            },{
                type: 'line',
                data: returnArray[6],
                name: tickers[6]
            },{
                type: 'line',
                data: returnArray[7],
                name: tickers[7]
            },{
                type: 'line',
                data: returnArray[8],
                name: tickers[8]
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

var universalColWidth=170;
var universalTableLeft=10;
var valueDecimals = 3;

function drawTableChartForOne(indicatorHeading,indicatorData,tickers){
	Highcharts.drawTable = function() {   
    // user options
    var tableTop = 10,
        colWidth = screen.width!=1920 ? universalColWidth : 300,
        tableLeft = universalTableLeft,
        rowHeight = 20,
        cellPadding = 2.5,
        valueSuffix = '';
        
    // internal variables
    var chart = this,
        series = chart.series,
        renderer = chart.renderer,
        cellLeft = tableLeft;

    // draw category labels
    $.each(chart.xAxis[0].categories, function(i, name) {
        renderer.text(
            name, 
            cellLeft + cellPadding, 
            tableTop + (i + 2) * rowHeight - cellPadding
        )
        .css({
            fontWeight: 'bold'
        })       
        .add();
    });

    $.each(series, function(i, serie) {
        cellLeft += colWidth;        
        // Apply the cell text
        renderer.text(
                '  '+serie.name,
                cellLeft - cellPadding + colWidth, 
                tableTop + rowHeight - cellPadding
            )
            .attr({
                align: 'right'
            })
            .css({
                fontWeight: 'bold'
            })
            .add();
        
        $.each(serie.data, function(row, point) {           
            // Apply the cell text
            if(point.y==99999999){
				renderer.text(
                    'N/A', 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  		
			}else{
				renderer.text(
                    Highcharts.numberFormat(point.y, valueDecimals) + valueSuffix, 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  	
			}              
            // horizontal lines
            if (row == 0) {
                Highcharts.tableLine( // top
                    renderer,
                    tableLeft, 
                    tableTop + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + cellPadding
                );
                Highcharts.tableLine( // bottom
                    renderer,
                    tableLeft, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding
                );
            }
            // horizontal line
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + row * rowHeight + rowHeight + cellPadding,
                cellLeft + colWidth, 
                tableTop + row * rowHeight + rowHeight + cellPadding
            );
                
        });      
        // vertical lines        
        if (i == 0) { // left table border  
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + cellPadding,
                tableLeft, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            );
        }       
        Highcharts.tableLine(
            renderer,
            cellLeft, 
            tableTop + cellPadding,
            cellLeft, 
            tableTop + (serie.data.length + 1) * rowHeight + cellPadding
        );          
        if (i == series.length - 1) { // right table border    
            Highcharts.tableLine(
                renderer,
                cellLeft + colWidth, 
                tableTop + cellPadding,
                cellLeft + colWidth, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            	);
        	}
        
    	});  
	};
	/**
	 * Draw a single line in the table
	 */
	Highcharts.tableLine = function (renderer, x1, y1, x2, y2) {
    	renderer.path(['M', x1, y1, 'L', x2, y2])
        	.attr({
            	'stroke': 'silver',
            	'stroke-width': 1
        	})
        	.add();
	}
	/**
	 * Create the chart
	 */
 	window.chart = new Highcharts.Chart({	
	    chart: {
	        renderTo: 'datatable-chart',
	        events: {
	            load: Highcharts.drawTable
	        },
	        height: 480,
	        //width: 1200,
	        scrollablePlotArea: {
	            minWidth: 380,
	            scrollPositionX: 0
	        }
	    },	    
	    title: {
	        text: ''
	    },	    
	    xAxis: {
	    		visible: false,
	        categories: indicatorHeading
	    },	    
	    yAxis: {
	        visible: false
	    },	
	    legend: {
	        enabled: false
	    },
	    plotOptions: {
	    	series: {
	      	visible: false
	      }
	    },
		credits: {
        	enabled: false
    	},
	    series: [{
	         name: tickers[0],
	         data: indicatorData[0]
	      }],
	      exporting: {
		        enabled: false,
		  },
	});
}

function drawTableChartForTwo(indicatorHeading,indicatorData,tickers){
	Highcharts.drawTable = function() {
    
    // user options
    var tableTop = 10,
        colWidth = screen.width!=1920 ? universalColWidth : 300,
        tableLeft = universalTableLeft,
        rowHeight = 20,
        cellPadding = 2.5,
        valueSuffix = '';
        
    // internal variables
    var chart = this,
        series = chart.series,
        renderer = chart.renderer,
        cellLeft = tableLeft;

    // draw category labels
    $.each(chart.xAxis[0].categories, function(i, name) {
        renderer.text(
            name, 
            cellLeft + cellPadding, 
            tableTop + (i + 2) * rowHeight - cellPadding
        )
        .css({
            fontWeight: 'bold'
        })       
        .add();
    });

    $.each(series, function(i, serie) {
        cellLeft += colWidth;        
        // Apply the cell text
        renderer.text(
                serie.name,
                cellLeft - cellPadding + colWidth, 
                tableTop + rowHeight - cellPadding
            )
            .attr({
                align: 'right'
            })
            .css({
                fontWeight: 'bold'
            })
            .add();
        
        $.each(serie.data, function(row, point) {
            
            // Apply the cell text
            if(point.y==99999999){
				renderer.text(
                    'N/A', 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  		
			}else{
				renderer.text(
                    Highcharts.numberFormat(point.y, valueDecimals) + valueSuffix, 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  	
			}              
            // horizontal lines
            if (row == 0) {
                Highcharts.tableLine( // top
                    renderer,
                    tableLeft, 
                    tableTop + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + cellPadding
                );
                Highcharts.tableLine( // bottom
                    renderer,
                    tableLeft, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding
                );
            }
            // horizontal line
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + row * rowHeight + rowHeight + cellPadding,
                cellLeft + colWidth, 
                tableTop + row * rowHeight + rowHeight + cellPadding
            );               
        });       
        // vertical lines        
        if (i == 0) { // left table border  
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + cellPadding,
                tableLeft, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            );
        }        
        Highcharts.tableLine(
            renderer,
            cellLeft, 
            tableTop + cellPadding,
            cellLeft, 
            tableTop + (serie.data.length + 1) * rowHeight + cellPadding
        );           
        if (i == series.length - 1) { // right table border    
            Highcharts.tableLine(
                renderer,
                cellLeft + colWidth, 
                tableTop + cellPadding,
                cellLeft + colWidth, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            	);
        	}
        
    	});  
	};
	/**
	 * Draw a single line in the table
	 */
	Highcharts.tableLine = function (renderer, x1, y1, x2, y2) {
    	renderer.path(['M', x1, y1, 'L', x2, y2])
        	.attr({
            	'stroke': 'silver',
            	'stroke-width': 1
        	})
        	.add();
	}
	/**
	 * Create the chart
	 */
	window.chart = new Highcharts.Chart({	
	    chart: {
	        renderTo: 'datatable-chart',
	        events: {
	            load: Highcharts.drawTable
	        },
	        height: 480,
	        scrollablePlotArea: {
	            minWidth: 550,
	            scrollPositionX: 0
	        }
	    },	    
	    title: {
	        text: ''
	    },	    
	    xAxis: {
	    		visible: false,
	        categories: indicatorHeading
	    },	    
	    yAxis: {
	        visible: false
	    },	
	    legend: {
	        enabled: false
	    },
	    plotOptions: {
	    	series: {
	      	visible: false
	      }
	    },
	    credits: {
        	enabled: false
    	},	
	    series: [{
	         name: tickers[0],
	         data: indicatorData[0]
	      },{
	         name: tickers[1],
	         data: indicatorData[1]
	      }],
	      exporting: {
		        enabled: false,
		  },
	});
}

function drawTableChartForThree(indicatorHeading,indicatorData,tickers){
	Highcharts.drawTable = function() {
    
    // user options
    var tableTop = 10,
        colWidth = screen.width!=1920 ? universalColWidth : 300,
        tableLeft = universalTableLeft,
        rowHeight = 20,
        cellPadding = 2.5,
        valueSuffix = '';
        
    // internal variables
    var chart = this,
        series = chart.series,
        renderer = chart.renderer,
        cellLeft = tableLeft;

    // draw category labels
    $.each(chart.xAxis[0].categories, function(i, name) {
        renderer.text(
            name, 
            cellLeft + cellPadding, 
            tableTop + (i + 2) * rowHeight - cellPadding
        )
        .css({
            fontWeight: 'bold'
        })       
        .add();
    });

    $.each(series, function(i, serie) {
        cellLeft += colWidth;        
        // Apply the cell text
        renderer.text(
                serie.name,
                cellLeft - cellPadding + colWidth, 
                tableTop + rowHeight - cellPadding
            )
            .attr({
                align: 'right'
            })
            .css({
                fontWeight: 'bold'
            })
            .add();
        
        $.each(serie.data, function(row, point) {
            
            // Apply the cell text
            if(point.y==99999999){
				renderer.text(
                    'N/A', 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  		
			}else{
				renderer.text(
                    Highcharts.numberFormat(point.y, valueDecimals) + valueSuffix, 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  	
			}               
            // horizontal lines
            if (row == 0) {
                Highcharts.tableLine( // top
                    renderer,
                    tableLeft, 
                    tableTop + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + cellPadding
                );
                Highcharts.tableLine( // bottom
                    renderer,
                    tableLeft, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding
                );
            }
            // horizontal line
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + row * rowHeight + rowHeight + cellPadding,
                cellLeft + colWidth, 
                tableTop + row * rowHeight + rowHeight + cellPadding
            );               
        });       
        // vertical lines        
        if (i == 0) { // left table border  
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + cellPadding,
                tableLeft, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            );
        }        
        Highcharts.tableLine(
            renderer,
            cellLeft, 
            tableTop + cellPadding,
            cellLeft, 
            tableTop + (serie.data.length + 1) * rowHeight + cellPadding
        );           
        if (i == series.length - 1) { // right table border    
            Highcharts.tableLine(
                renderer,
                cellLeft + colWidth, 
                tableTop + cellPadding,
                cellLeft + colWidth, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            	);
        	}
        
    	});  
	};
	/**
	 * Draw a single line in the table
	 */
	Highcharts.tableLine = function (renderer, x1, y1, x2, y2) {
    	renderer.path(['M', x1, y1, 'L', x2, y2])
        	.attr({
            	'stroke': 'silver',
            	'stroke-width': 1
        	})
        	.add();
	}
	/**
	 * Create the chart
	 */
	window.chart = new Highcharts.Chart({	
	    chart: {
	        renderTo: 'datatable-chart',
	        events: {
	            load: Highcharts.drawTable
	        },
	        height: 480,
	        scrollablePlotArea: {
	            minWidth: 720,
	            scrollPositionX: 0
	        }
	    },	    
	    title: {
	        text: ''
	    },	    
	    xAxis: {
	    		visible: false,
	        categories: indicatorHeading
	    },	    
	    yAxis: {
	        visible: false
	    },	
	    legend: {
	        enabled: false
	    },
	    plotOptions: {
	    	series: {
	      	visible: false
	      }
	    },	
	    credits: {
        	enabled: false
    	},
	    series: [{
	         name: tickers[0],
	         data: indicatorData[0]
	      },{
	         name: tickers[1],
	         data: indicatorData[1]
	      } ,{
	         name: tickers[2],
	         data: indicatorData[2]
	      }],
	      exporting: {
		        enabled: false,
		  },
	});
}

function drawTableChartForFour(indicatorHeading,indicatorData,tickers){
	Highcharts.drawTable = function() {
    
    // user options
    var tableTop = 10,
        colWidth = screen.width!=1920 ? universalColWidth : 250,
        tableLeft = universalTableLeft,
        rowHeight = 25,
        cellPadding = 2.5,
        valueSuffix = '';
        
    // internal variables
    var chart = this,
        series = chart.series,
        renderer = chart.renderer,
        cellLeft = tableLeft;

    // draw category labels
    $.each(chart.xAxis[0].categories, function(i, name) {
        renderer.text(
            name, 
            cellLeft + cellPadding, 
            tableTop + (i + 2) * rowHeight - cellPadding
        )
        .css({
            fontWeight: 'bold'
        })       
        .add();
    });

    $.each(series, function(i, serie) {
        cellLeft += colWidth;        
        // Apply the cell text
        renderer.text(
                serie.name,
                cellLeft - cellPadding + colWidth, 
                tableTop + rowHeight - cellPadding
            )
            .attr({
                align: 'right'
            })
            .css({
                fontWeight: 'bold'
            })
            .add();
        
        $.each(serie.data, function(row, point) {
            
            // Apply the cell text
            if(point.y==99999999){
				renderer.text(
                    'N/A', 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  		
			}else{
				renderer.text(
                    Highcharts.numberFormat(point.y, valueDecimals) + valueSuffix, 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  	
			}              
            // horizontal lines
            if (row == 0) {
                Highcharts.tableLine( // top
                    renderer,
                    tableLeft, 
                    tableTop + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + cellPadding
                );
                Highcharts.tableLine( // bottom
                    renderer,
                    tableLeft, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding
                );
            }
            // horizontal line
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + row * rowHeight + rowHeight + cellPadding,
                cellLeft + colWidth, 
                tableTop + row * rowHeight + rowHeight + cellPadding
            );               
        });       
        // vertical lines        
        if (i == 0) { // left table border  
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + cellPadding,
                tableLeft, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            );
        }        
        Highcharts.tableLine(
            renderer,
            cellLeft, 
            tableTop + cellPadding,
            cellLeft, 
            tableTop + (serie.data.length + 1) * rowHeight + cellPadding
        );           
        if (i == series.length - 1) { // right table border    
            Highcharts.tableLine(
                renderer,
                cellLeft + colWidth, 
                tableTop + cellPadding,
                cellLeft + colWidth, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            	);
        	}
        
    	});  
	};

	/**
	 * Draw a single line in the table
	 */
	Highcharts.tableLine = function (renderer, x1, y1, x2, y2) {
    	renderer.path(['M', x1, y1, 'L', x2, y2])
        	.attr({
            	'stroke': 'silver',
            	'stroke-width': 1
        	})
        	.add();
	}
	/**
	 * Create the chart
	 */
	window.chart = new Highcharts.Chart({	
	    chart: {
	        renderTo: 'datatable-chart',
	        events: {
	            load: Highcharts.drawTable
	        },
	        height: 580,
	        scrollablePlotArea: {
	            minWidth: 880,
	            scrollPositionX: 0
	        }
	    },	    
	    title: {
	        text: ''
	    },	    
	    xAxis: {
	    		visible: false,
	        categories: indicatorHeading
	    },	    
	    yAxis: {
	        visible: false
	    },	
	    legend: {
	        enabled: false
	    },
	    plotOptions: {
	    	series: {
	      	visible: false
	      }
	    },	
	    credits: {
        	enabled: false
    	},
	    series: [{
	         name: tickers[0],
	         data: indicatorData[0]
	      },{
	         name: tickers[1],
	         data: indicatorData[1]
	      },{
	         name: tickers[2],
	         data: indicatorData[2]
	      },{
	         name: tickers[3],
	         data: indicatorData[3]
	      }],
	      exporting: {
		        enabled: false,
		  },
	});
}

function drawTableChartForFive(indicatorHeading,indicatorData,tickers){
	Highcharts.drawTable = function() {
    
    // user options
    var tableTop = 10,
        colWidth = screen.width!=1920 ? universalColWidth : 200,
        tableLeft = universalTableLeft,
        rowHeight = 25,
        cellPadding = 2.5,
        valueSuffix = '';
        
    // internal variables
    var chart = this,
        series = chart.series,
        renderer = chart.renderer,
        cellLeft = tableLeft;

    // draw category labels
    $.each(chart.xAxis[0].categories, function(i, name) {
        renderer.text(
            name, 
            cellLeft + cellPadding, 
            tableTop + (i + 2) * rowHeight - cellPadding
        )
        .css({
            fontWeight: 'bold'
        })       
        .add();
    });

    $.each(series, function(i, serie) {
        cellLeft += colWidth;        
        // Apply the cell text
        renderer.text(
                serie.name,
                cellLeft - cellPadding + colWidth, 
                tableTop + rowHeight - cellPadding
            )
            .attr({
                align: 'right'
            })
            .css({
                fontWeight: 'bold',
            })
            .add();
        
        $.each(serie.data, function(row, point) {           
            // Apply the cell text
            if(point.y==99999999){
				renderer.text(
                    'N/A', 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  		
			}else{
				renderer.text(
                    Highcharts.numberFormat(point.y, valueDecimals) + valueSuffix, 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  	
			}          
            // horizontal lines
            if (row == 0) {
                Highcharts.tableLine( // top
                    renderer,
                    tableLeft, 
                    tableTop + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + cellPadding
                );
                Highcharts.tableLine( // bottom
                    renderer,
                    tableLeft, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding
                );
            }
            // horizontal line
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + row * rowHeight + rowHeight + cellPadding,
                cellLeft + colWidth, 
                tableTop + row * rowHeight + rowHeight + cellPadding
            );               
        });       
        // vertical lines        
        if (i == 0) { // left table border  
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + cellPadding,
                tableLeft, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            );
        }        
        Highcharts.tableLine(
            renderer,
            cellLeft, 
            tableTop + cellPadding,
            cellLeft, 
            tableTop + (serie.data.length + 1) * rowHeight + cellPadding
        );           
        if (i == series.length - 1) { // right table border    
            Highcharts.tableLine(
                renderer,
                cellLeft + colWidth, 
                tableTop + cellPadding,
                cellLeft + colWidth, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            	);
        	}
        
    	});  
	};

	/**
	 * Draw a single line in the table
	 */
	Highcharts.tableLine = function (renderer, x1, y1, x2, y2) {
    	renderer.path(['M', x1, y1, 'L', x2, y2])
        	.attr({
            	'stroke': 'silver',
            	'stroke-width': 1
        	})
        	.add();
	}
	/**
	 * Create the chart
	 */
	window.chart = new Highcharts.Chart({	
	    chart: {
	        renderTo: 'datatable-chart',
	        events: {
	            load: Highcharts.drawTable
	        },
	        height: 580,
	        scrollablePlotArea: {
	            minWidth: 1050,
	            scrollPositionX: 0
	        }
	    },	    
	    title: {
	        text: ''
	    },	    
	    xAxis: {
	    		visible: false,
	        categories: indicatorHeading
	    },	    
	    yAxis: {
	        visible: false
	    },	
	    legend: {
	        enabled: false
	    },
	    plotOptions: {
	    	series: {
	      	visible: false
	      }
	    },	
	    credits: {
        	enabled: false
    	},
	    series: [{
	         name: tickers[0],
	         data: indicatorData[0]
	      },{
	         name: tickers[1],
	         data: indicatorData[1]
	      },{
	         name: tickers[2],
	         data: indicatorData[2]
	      },{
	         name: tickers[3],
	         data: indicatorData[3]
	      },{
	         name: tickers[4],
	         data: indicatorData[4]
	      }],
	      exporting: {
		        enabled: false,
		  },
	});
}

function drawTableChartForSix(indicatorHeading,indicatorData,tickers){
	Highcharts.drawTable = function() {
    
    // user options
    var tableTop = 10,
        colWidth = 225,
        tableLeft = universalTableLeft,
        rowHeight = 25,
        cellPadding = 2.5,
        valueSuffix = '';
        
    // internal variables
    var chart = this,
        series = chart.series,
        renderer = chart.renderer,
        cellLeft = tableLeft;

    // draw category labels
    $.each(chart.xAxis[0].categories, function(i, name) {
        renderer.text(
            name, 
            cellLeft + cellPadding, 
            tableTop + (i + 2) * rowHeight - cellPadding
        )
        .css({
            fontWeight: 'bold'
        })       
        .add();
    });

    $.each(series, function(i, serie) {
        cellLeft += colWidth;        
        // Apply the cell text
        renderer.text(
                serie.name,
                cellLeft - cellPadding + colWidth, 
                tableTop + rowHeight - cellPadding
            )
            .attr({
                align: 'right'
            })
            .css({
                fontWeight: 'bold',
            })
            .add();
        
        $.each(serie.data, function(row, point) {           
            // Apply the cell text
            if(point.y==99999999){
				renderer.text(
                    'N/A', 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  		
			}else{
				renderer.text(
                    Highcharts.numberFormat(point.y, valueDecimals) + valueSuffix, 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  	
			}          
            // horizontal lines
            if (row == 0) {
                Highcharts.tableLine( // top
                    renderer,
                    tableLeft, 
                    tableTop + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + cellPadding
                );
                Highcharts.tableLine( // bottom
                    renderer,
                    tableLeft, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding
                );
            }
            // horizontal line
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + row * rowHeight + rowHeight + cellPadding,
                cellLeft + colWidth, 
                tableTop + row * rowHeight + rowHeight + cellPadding
            );               
        });       
        // vertical lines        
        if (i == 0) { // left table border  
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + cellPadding,
                tableLeft, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            );
        }        
        Highcharts.tableLine(
            renderer,
            cellLeft, 
            tableTop + cellPadding,
            cellLeft, 
            tableTop + (serie.data.length + 1) * rowHeight + cellPadding
        );           
        if (i == series.length - 1) { // right table border    
            Highcharts.tableLine(
                renderer,
                cellLeft + colWidth, 
                tableTop + cellPadding,
                cellLeft + colWidth, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            	);
        	}
        
    	});  
	};

	/**
	 * Draw a single line in the table
	 */
	Highcharts.tableLine = function (renderer, x1, y1, x2, y2) {
    	renderer.path(['M', x1, y1, 'L', x2, y2])
        	.attr({
            	'stroke': 'silver',
            	'stroke-width': 1
        	})
        	.add();
	}
	/**
	 * Create the chart
	 */
	window.chart = new Highcharts.Chart({	
	    chart: {
	        renderTo: 'datatable-chart',
	        events: {
	            load: Highcharts.drawTable
	        },
	        height: 580,
	        scrollablePlotArea: {
	            minWidth: 1250,
	            scrollPositionX: 0
	        }
	    },	    
	    title: {
	        text: ''
	    },	    
	    xAxis: {
	    		visible: false,
	        categories: indicatorHeading
	    },	    
	    yAxis: {
	        visible: false
	    },	
	    legend: {
	        enabled: false
	    },
	    plotOptions: {
	    	series: {
	      	visible: false
	      }
	    },	
	    credits: {
        	enabled: false
    	},
	    series: [{
	         name: tickers[0],
	         data: indicatorData[0]
	      },{
	         name: tickers[1],
	         data: indicatorData[1]
	      },{
	         name: tickers[2],
	         data: indicatorData[2]
	      },{
	         name: tickers[3],
	         data: indicatorData[3]
	      },{
	         name: tickers[4],
	         data: indicatorData[4]
	      },{
	         name: tickers[5],
	         data: indicatorData[5]
	      }],
	      exporting: {
		        enabled: false,
		  },
	});
}

function drawTableChartForSeven(indicatorHeading,indicatorData,tickers){
	Highcharts.drawTable = function() {
    
    // user options
    var tableTop = 10,
        colWidth = 210,
        tableLeft = universalTableLeft,
        rowHeight = 25,
        cellPadding = 2.5,
        valueSuffix = '';
        
    // internal variables
    var chart = this,
        series = chart.series,
        renderer = chart.renderer,
        cellLeft = tableLeft;

    // draw category labels
    $.each(chart.xAxis[0].categories, function(i, name) {
        renderer.text(
            name, 
            cellLeft + cellPadding, 
            tableTop + (i + 2) * rowHeight - cellPadding
        )
        .css({
            fontWeight: 'bold'
        })       
        .add();
    });

    $.each(series, function(i, serie) {
        cellLeft += colWidth;        
        // Apply the cell text
        renderer.text(
                serie.name,
                cellLeft - cellPadding + colWidth, 
                tableTop + rowHeight - cellPadding
            )
            .attr({
                align: 'right'
            })
            .css({
                fontWeight: 'bold',
            })
            .add();
        
        $.each(serie.data, function(row, point) {           
            // Apply the cell text
            if(point.y==99999999){
				renderer.text(
                    'N/A', 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  		
			}else{
				renderer.text(
                    Highcharts.numberFormat(point.y, valueDecimals) + valueSuffix, 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  	
			}          
            // horizontal lines
            if (row == 0) {
                Highcharts.tableLine( // top
                    renderer,
                    tableLeft, 
                    tableTop + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + cellPadding
                );
                Highcharts.tableLine( // bottom
                    renderer,
                    tableLeft, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding
                );
            }
            // horizontal line
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + row * rowHeight + rowHeight + cellPadding,
                cellLeft + colWidth, 
                tableTop + row * rowHeight + rowHeight + cellPadding
            );               
        });       
        // vertical lines        
        if (i == 0) { // left table border  
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + cellPadding,
                tableLeft, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            );
        }        
        Highcharts.tableLine(
            renderer,
            cellLeft, 
            tableTop + cellPadding,
            cellLeft, 
            tableTop + (serie.data.length + 1) * rowHeight + cellPadding
        );           
        if (i == series.length - 1) { // right table border    
            Highcharts.tableLine(
                renderer,
                cellLeft + colWidth, 
                tableTop + cellPadding,
                cellLeft + colWidth, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            	);
        	}
        
    	});  
	};

	/**
	 * Draw a single line in the table
	 */
	Highcharts.tableLine = function (renderer, x1, y1, x2, y2) {
    	renderer.path(['M', x1, y1, 'L', x2, y2])
        	.attr({
            	'stroke': 'silver',
            	'stroke-width': 1
        	})
        	.add();
	}
	/**
	 * Create the chart
	 */
	window.chart = new Highcharts.Chart({	
	    chart: {
	        renderTo: 'datatable-chart',
	        events: {
	            load: Highcharts.drawTable
	        },
	        height: 600,
	        scrollablePlotArea: {
	            minWidth: 1050,
	            scrollPositionX: 0
	        }
	    },	    
	    title: {
	        text: ''
	    },	    
	    xAxis: {
	    		visible: false,
	        categories: indicatorHeading
	    },	    
	    yAxis: {
	        visible: false
	    },	
	    legend: {
	        enabled: false
	    },
	    plotOptions: {
	    	series: {
	      	visible: false
	      }
	    },	
	    credits: {
        	enabled: false
    	},
	    series: [{
	         name: tickers[0],
	         data: indicatorData[0]
	      },{
	         name: tickers[1],
	         data: indicatorData[1]
	      },{
	         name: tickers[2],
	         data: indicatorData[2]
	      },{
	         name: tickers[3],
	         data: indicatorData[3]
	      },{
	         name: tickers[4],
	         data: indicatorData[4]
	      },{
	         name: tickers[5],
	         data: indicatorData[5]
	      },{
	         name: tickers[6],
	         data: indicatorData[6]
	      }],
	      exporting: {
		        enabled: false,
		  },
	});
}

function drawTableChartForEight(indicatorHeading,indicatorData,tickers){
	Highcharts.drawTable = function() {
    
    // user options
    var tableTop = 10,
        colWidth = 200,
        tableLeft = universalTableLeft,
        rowHeight = 25,
        cellPadding = 2.5,
        valueSuffix = '';
        
    // internal variables
    var chart = this,
        series = chart.series,
        renderer = chart.renderer,
        cellLeft = tableLeft;

    // draw category labels
    $.each(chart.xAxis[0].categories, function(i, name) {
        renderer.text(
            name, 
            cellLeft + cellPadding, 
            tableTop + (i + 2) * rowHeight - cellPadding
        )
        .css({
            fontWeight: 'bold'
        })       
        .add();
    });

    $.each(series, function(i, serie) {
        cellLeft += colWidth;        
        // Apply the cell text
        renderer.text(
                serie.name,
                cellLeft - cellPadding + colWidth, 
                tableTop + rowHeight - cellPadding
            )
            .attr({
                align: 'right'
            })
            .css({
                fontWeight: 'bold',
            })
            .add();
        
        $.each(serie.data, function(row, point) {           
            // Apply the cell text
            if(point.y==99999999){
				renderer.text(
                    'N/A', 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  		
			}else{
				renderer.text(
                    Highcharts.numberFormat(point.y, valueDecimals) + valueSuffix, 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  	
			}          
            // horizontal lines
            if (row == 0) {
                Highcharts.tableLine( // top
                    renderer,
                    tableLeft, 
                    tableTop + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + cellPadding
                );
                Highcharts.tableLine( // bottom
                    renderer,
                    tableLeft, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding
                );
            }
            // horizontal line
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + row * rowHeight + rowHeight + cellPadding,
                cellLeft + colWidth, 
                tableTop + row * rowHeight + rowHeight + cellPadding
            );               
        });       
        // vertical lines        
        if (i == 0) { // left table border  
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + cellPadding,
                tableLeft, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            );
        }        
        Highcharts.tableLine(
            renderer,
            cellLeft, 
            tableTop + cellPadding,
            cellLeft, 
            tableTop + (serie.data.length + 1) * rowHeight + cellPadding
        );           
        if (i == series.length - 1) { // right table border    
            Highcharts.tableLine(
                renderer,
                cellLeft + colWidth, 
                tableTop + cellPadding,
                cellLeft + colWidth, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            	);
        	}
        
    	});  
	};

	/**
	 * Draw a single line in the table
	 */
	Highcharts.tableLine = function (renderer, x1, y1, x2, y2) {
    	renderer.path(['M', x1, y1, 'L', x2, y2])
        	.attr({
            	'stroke': 'silver',
            	'stroke-width': 1
        	})
        	.add();
	}
	/**
	 * Create the chart
	 */
	window.chart = new Highcharts.Chart({	
	    chart: {
	        renderTo: 'datatable-chart',
	        events: {
	            load: Highcharts.drawTable
	        },
	        height: 600,
	        scrollablePlotArea: {
	            minWidth: 1050,
	            scrollPositionX: 0
	        }
	    },	    
	    title: {
	        text: ''
	    },	    
	    xAxis: {
	    		visible: false,
	        categories: indicatorHeading
	    },	    
	    yAxis: {
	        visible: false
	    },	
	    legend: {
	        enabled: false
	    },
	    plotOptions: {
	    	series: {
	      	visible: false
	      }
	    },	
	    credits: {
        	enabled: false
    	},
	    series: [{
	         name: tickers[0],
	         data: indicatorData[0]
	      },{
	         name: tickers[1],
	         data: indicatorData[1]
	      },{
	         name: tickers[2],
	         data: indicatorData[2]
	      },{
	         name: tickers[3],
	         data: indicatorData[3]
	      },{
	         name: tickers[4],
	         data: indicatorData[4]
	      },{
	         name: tickers[5],
	         data: indicatorData[5]
	      },{
	         name: tickers[6],
	         data: indicatorData[6]
	      },{
	         name: tickers[7],
	         data: indicatorData[7]
	      }],
	      exporting: {
		        enabled: false,
		  },
	});
}

function drawTableChartForNine(indicatorHeading,indicatorData,tickers){
	Highcharts.drawTable = function() {
    
    // user options
    var tableTop = 10,
        colWidth = 180,
        tableLeft = universalTableLeft,
        rowHeight = 25,
        cellPadding = 2.5,
        valueSuffix = '';
        
    // internal variables
    var chart = this,
        series = chart.series,
        renderer = chart.renderer,
        cellLeft = tableLeft;

    // draw category labels
    $.each(chart.xAxis[0].categories, function(i, name) {
        renderer.text(
            name, 
            cellLeft + cellPadding, 
            tableTop + (i + 2) * rowHeight - cellPadding
        )
        .css({
            fontWeight: 'bold'
        })       
        .add();
    });

    $.each(series, function(i, serie) {
        cellLeft += colWidth;        
        // Apply the cell text
        renderer.text(
                serie.name,
                cellLeft - cellPadding + colWidth, 
                tableTop + rowHeight - cellPadding
            )
            .attr({
                align: 'right'
            })
            .css({
                fontWeight: 'bold',
            })
            .add();
        
        $.each(serie.data, function(row, point) {           
            // Apply the cell text
            if(point.y==99999999){
				renderer.text(
                    'N/A', 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  		
			}else{
				renderer.text(
                    Highcharts.numberFormat(point.y, valueDecimals) + valueSuffix, 
                    cellLeft + colWidth - cellPadding, 
                    tableTop + (row + 2) * rowHeight - cellPadding
                )
                .attr({
                    align: 'right'
                })
                .add();  	
			}          
            // horizontal lines
            if (row == 0) {
                Highcharts.tableLine( // top
                    renderer,
                    tableLeft, 
                    tableTop + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + cellPadding
                );
                Highcharts.tableLine( // bottom
                    renderer,
                    tableLeft, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding,
                    cellLeft + colWidth, 
                    tableTop + (serie.data.length + 1) * rowHeight + cellPadding
                );
            }
            // horizontal line
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + row * rowHeight + rowHeight + cellPadding,
                cellLeft + colWidth, 
                tableTop + row * rowHeight + rowHeight + cellPadding
            );               
        });       
        // vertical lines        
        if (i == 0) { // left table border  
            Highcharts.tableLine(
                renderer,
                tableLeft, 
                tableTop + cellPadding,
                tableLeft, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            );
        }        
        Highcharts.tableLine(
            renderer,
            cellLeft, 
            tableTop + cellPadding,
            cellLeft, 
            tableTop + (serie.data.length + 1) * rowHeight + cellPadding
        );           
        if (i == series.length - 1) { // right table border    
            Highcharts.tableLine(
                renderer,
                cellLeft + colWidth, 
                tableTop + cellPadding,
                cellLeft + colWidth, 
                tableTop + (serie.data.length + 1) * rowHeight + cellPadding
            	);
        	}
        
    	});  
	};

	/**
	 * Draw a single line in the table
	 */
	Highcharts.tableLine = function (renderer, x1, y1, x2, y2) {
    	renderer.path(['M', x1, y1, 'L', x2, y2])
        	.attr({
            	'stroke': 'silver',
            	'stroke-width': 1
        	})
        	.add();
	}
	/**
	 * Create the chart
	 */
	window.chart = new Highcharts.Chart({	
	    chart: {
	        renderTo: 'datatable-chart',
	        events: {
	            load: Highcharts.drawTable
	        },
	        height: 600,
	        scrollablePlotArea: {
	            minWidth: 1050,
	            scrollPositionX: 0
	        }
	    },	    
	    title: {
	        text: ''
	    },	    
	    xAxis: {
	    		visible: false,
	        categories: indicatorHeading
	    },	    
	    yAxis: {
	        visible: false
	    },	
	    legend: {
	        enabled: false
	    },
	    plotOptions: {
	    	series: {
	      	visible: false
	      }
	    },	
	    credits: {
        	enabled: false
    	},
	    series: [{
	         name: tickers[0],
	         data: indicatorData[0]
	      },{
	         name: tickers[1],
	         data: indicatorData[1]
	      },{
	         name: tickers[2],
	         data: indicatorData[2]
	      },{
	         name: tickers[3],
	         data: indicatorData[3]
	      },{
	         name: tickers[4],
	         data: indicatorData[4]
	      },{
	         name: tickers[5],
	         data: indicatorData[5]
	      },{
	         name: tickers[6],
	         data: indicatorData[6]
	      },{
	         name: tickers[7],
	         data: indicatorData[7]
	      },{
	         name: tickers[8],
	         data: indicatorData[8]
	      }],
	      exporting: {
		        enabled: false,
		  },
	});
}