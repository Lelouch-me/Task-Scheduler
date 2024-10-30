   	  	function changeBasicRetAge() {
    	  	var x = document.getElementById("basic_retirement_age").value;
    	  	document.getElementById("b_ret_age").innerHTML = x;
    	  	document.getElementById("b_ret_age2").innerHTML = x;
    	}
    	
    	function resetBasicValues() {
    		document.getElementById('b_age').value = '30';
    		document.getElementById('basic_retirement_age').value = '60';
    		//document.getElementById('b_expected_income').value = '700,000';
    		//document.getElementById('b_initial_investment').value = '100,000';
    		//document.getElementById('b_yearly_investment').value = '24,000';
    	}
    	
    	function resetCustomizedValues() {
    		document.getElementById('c_age').value = '30';
    		document.getElementById('c_retirement_age').value = '60';
    		document.getElementById('c_gender').value = 'Male';
    		document.getElementById('c_spouse').value = 'None';
    		document.getElementById('c_spouse_age').value = '';
    		disableSpouseAge();
    		//document.getElementById('c_initial_investment').value = '100,000';
    		//document.getElementById('shortTerm').value = '15,000';
    		//document.getElementById('midTerm').value = '24,000';
        	//document.getElementById('longTerm').value = '700,000';
        	removeTableData();
    	}
    	
    	function disableSpouseAge(){
    		var s = document.getElementById("c_spouse").value;
        	if(s=='None'){
        		document.getElementById("c_spouse_age").readOnly = true;
        	}else{
        		document.getElementById("c_spouse_age").readOnly = false;
        	}
    	}
    	
    	function changeContributionWithdrawlPeriod(){
    		const currentYear = new Date().getFullYear();
    		//document.getElementById("shortTermPeriod").innerHTML = "From Now To December " + currentYear;
    		
    		var nextYear = currentYear + 1;
    		var curr_age;
    		if(document.getElementById("c_age").value==''){
    			curr_age = 30;
    		}else{
    			curr_age = document.getElementById("c_age").value;
    		}
    		
    		var activeYear = document.getElementById("c_retirement_age").value - curr_age;
    		var retYear = currentYear + activeYear;
    		document.getElementById("midTermPeriod").innerHTML = "From "+nextYear+ " To " + retYear;
    		
    		var nextRetYear = retYear + 1 ;
    		var endYear = currentYear + 115 - curr_age;
    		document.getElementById("longTermPeriod").innerHTML = "From "+nextRetYear+ " To " + endYear;
    		
    		var s = document.getElementById("termSelection").value;
    		if(s=='1'){
    			document.getElementById("termStartYear").innerHTML = currentYear;
    			document.getElementById("termEndYear").innerHTML = currentYear;
    			document.getElementById("termStartYear").value = currentYear;
    			document.getElementById("termEndYear").value = currentYear;
    		}else if(s=='2'){
    			document.getElementById("termStartYear").innerHTML = nextYear;
    			document.getElementById("termEndYear").innerHTML = retYear;
    			document.getElementById("termStartYear").value = nextYear;
    			document.getElementById("termEndYear").value = retYear;
    		}else{
    			document.getElementById("termStartYear").innerHTML = nextRetYear;
    			document.getElementById("termEndYear").innerHTML = endYear;
    			document.getElementById("termStartYear").value = nextRetYear;
    			document.getElementById("termEndYear").value = endYear;
    		}
    	}
    	
    	function addTableRow(){
    		if(document.getElementById("cwName").value=='' || document.getElementById("cwAmount").value==''){
    			alert('Please Enter Correct Values!');
    		}else{
    			var table = document.getElementById("cwTable");
        		var row = table.insertRow(1);
        		var cell1 = row.insertCell(0);
        		var cell2 = row.insertCell(1);
        		var cell3 = row.insertCell(2);
        		var cell4 = row.insertCell(3);
        		
        		cell1.innerHTML = document.getElementById("cwName").value;
        		if(document.getElementById("frequencySelection").value=='2'){
        			cell2.innerHTML = document.getElementById("cwAmount").value * 12;
        		}else{
        			cell2.innerHTML = document.getElementById("cwAmount").value;
        		}
        		if(document.getElementById("typeSelection").value=='1'){
        			cell2.innerHTML = '+'+cell2.innerHTML;
        		}else{
        			cell2.innerHTML = '-'+cell2.innerHTML;
        		}
        		cell3.innerHTML = document.getElementById("termStartYear").value;
        		cell4.innerHTML = document.getElementById("termEndYear").value;
        		
        		$('#exampleModal').modal('hide');
        		document.getElementById("cwName").value = '';
        		document.getElementById("cwAmount").value = '';
    		}
    		
    	}
    	
    	function clearField(){
    		document.getElementById("cwName").value = '';
    		//document.getElementById("cwAmount").value = '';
    	}
    	
    	function removeTableData(){
    		var tableHeaderRowCount = 1;
    		var table = document.getElementById('cwTable');
    		var rowCount = table.rows.length;
    		for (var i = tableHeaderRowCount; i < rowCount; i++) {
    		    table.deleteRow(tableHeaderRowCount);
    		}
    	}
    	
    	function getActiveTabId(first,second){
    		var basicTab = document.getElementById(first).className;
    		//var cusTab = document.getElementById(second).className;
    		if(basicTab.includes("active")){
    			return first;
    		}else{
    			return second;
    		}
    	}
    	
    	function populateSelectBox(startValue,id){
			var x = document.getElementById(id);
			for(var i = startValue; i <=10000000; i = i + 100000){
				var option = document.createElement("option");
				option.value = i;
				option.text = i.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
				x.add(option);
			}
		}
		
function processInputJSON(){   	
	var modifiedPriceDtls;	
	var input_info;
    var m_age;
    var r_age;	
    var activeTabId = getActiveTabId("home-tab","profile-tab");
    if(activeTabId == "home-tab"){
		var age = parseInt(document.getElementById("b_age").value);
        var ret_age = parseInt(document.getElementById("basic_retirement_age").value);
        var initial_invest = document.getElementById("b_initial_investment").value;
        var exp_income = document.getElementById("b_expected_income").value;
        var yearly_invest = document.getElementById("b_yearly_investment").value;
        
    	if(age=='' || ret_age=='' || initial_invest=='' || exp_income=='' || yearly_invest=='' || age==0 || ret_age==0){
    		alert("Please enter correct value!");
    		document.getElementById('spinner').style.display = 'none';
            document.getElementById("result_button").disabled = false;
    	}else{ 			
        	if(ret_age>age){
        		initial_invest = initial_invest.replace(/,/g, '');
        		exp_income = exp_income.replace(/,/g, '');
        		yearly_invest = yearly_invest.replace(/,/g, '');
        		exp_income = '-'+exp_income;
        		m_age = age;
        		r_age = ret_age;
        				
        		var input_json = {
        			'contributionWithdrawalType' : 'ANNUAL',
        			'gender' : 'MALE',
        			'inflationrate' : 1,
        			'initialAssets' : parseInt(initial_invest),
        			'investorAge' : age,
        			'longTerm' : parseInt(exp_income),
        			'mediumTerm' : parseInt(yearly_invest),
        			'retirementAge' : ret_age,
        			'shortTerm' : parseInt(yearly_invest),
        			'spouse' : 'NONE',
        		}
        		//console.log(JSON.stringify(input_json));
        		input_info = input_json;
        				
        	}else{
        		alert("Target retirement age should be greater than current age!");
        		document.getElementById('spinner').style.display = 'none';
                document.getElementById("result_button").disabled = false;
        	}
    	}
    }else{
    	var age = parseInt(document.getElementById("c_age").value);
    	var ret_age = parseInt(document.getElementById("c_retirement_age").value);
    	var initial_invest = document.getElementById("c_initial_investment").value;
    	initial_invest = initial_invest.replace(/,/g, '');
    			
    	var gender = document.getElementById('c_gender').value;
        var spouse = document.getElementById('c_spouse').value;       		
        var spouseAge = document.getElementById('c_spouse_age').value;      
        		
        if(age=='' || ret_age=='' || initial_invest=='' || age==0 || ret_age==0){
    		alert("Please enter correct value!");
    		document.getElementById('spinner').style.display = 'none';
            document.getElementById("result_button").disabled = false;
    	}else{
    		if(ret_age>age){
            	if(spouse!='None' && spouseAge==''){
            		alert("Please enter spouse age!");
            		document.getElementById('spinner').style.display = 'none';
                    document.getElementById("result_button").disabled = false;
            	}else{
            		m_age = age;
            		r_age = ret_age;
            		var activeTabId2 = getActiveTabId("annual-tab","schedule-tab");
                    if(activeTabId2 == "annual-tab"){
                    	var shortTerm = document.getElementById('shortTerm').value;
                        var midTerm = document.getElementById('midTerm').value;
                        var longTerm = document.getElementById('longTerm').value;
                        		
                        shortTerm = shortTerm.replace(/,/g, '');
                        midTerm = midTerm.replace(/,/g, '');
                        longTerm = longTerm.replace(/,/g, '');
                        longTerm = '-'+longTerm;
                        		
                        if(shortTerm=='' || midTerm=='' || longTerm==''){
                    		alert("Please enter correct value!");
                    		document.getElementById('spinner').style.display = 'none';
                            document.getElementById("result_button").disabled = false;
                    	}else{
                    		var input_json = {
                    			'contributionWithdrawalType' : 'ANNUAL',
                    			'gender' : gender,
                    			'inflationrate' : 1,
                    			'initialAssets' : parseInt(initial_invest),
                    			'investorAge' : parseInt(age),
                    			'longTerm' : parseInt(longTerm),
                    			'mediumTerm' : parseInt(midTerm),
                    			'retirementAge' : parseInt(ret_age),
                    			'shortTerm' : parseInt(shortTerm),
                    		}
                            if(spouse=='None'){
                            	input_json.spouse = 'NONE';
                            }else{
                            	input_json.spouse = spouse;
                            	input_json.spouseAge = parseInt(spouseAge);
                            }
                    				//console.log(JSON.stringify(input_json));
                    		input_info = input_json;
                    	}
                    }else{
                    	var details = [];
                        var table = document.getElementById('cwTable');
                        var rowCount = table.rows.length;
                        if(rowCount<2){
                        	alert("Please enter correct value!");
                        	document.getElementById('spinner').style.display = 'none';
                            document.getElementById("result_button").disabled = false;
                        }else{
                        	for (var i = 1; i < rowCount; i++) {
                            	var oCells = table.rows.item(i).cells;
                            	var bl = {
                          			"annualAmount": parseInt(oCells.item(1).innerHTML.replace(/[+-]/g, '')),
                          			"endYear": parseInt(oCells.item(3).innerHTML),
                          			"name": oCells.item(0).innerHTML,
                          			"startYear": parseInt(oCells.item(2).innerHTML),
                          			"type": oCells.item(1).innerHTML.includes("+") ? 'CONTRIBUTION' : 'WITHDRAWAL'
                          		}
                            	details.push(bl);
                            }
                            var input_json = {
                    			'contributionWithdrawalType' : 'SCHEDULE',
                    			'gender' : gender,
                    			'inflationrate' : 1,
                    			'initialAssets' : parseInt(initial_invest),
                    			'investorAge' : parseInt(age),
                    			'retirementAge' : parseInt(ret_age),
                    		}
                            if(spouse=='None'){
                            	input_json.spouse = 'NONE';
                            }else{
                            	input_json.spouse = spouse;
                            	input_json.spouseAge = parseInt(spouseAge);
                            }
                            input_json.annualisedContributionWithdrawal = details;
                            //console.log(JSON.stringify(input_json));
                            input_info = input_json;
                        }
                    }
            	}
            }else{
            	alert("Target retirement age should be greater than current age!");
            	document.getElementById('spinner').style.display = 'none';
                document.getElementById("result_button").disabled = false;                
            }
    	}
    }
    
    $.ajax({
    	url : "/api/chartData/personalFinance",
        contentType: 'application/json',
        dataType: 'json',
        data : JSON.stringify(input_info),
        type : 'post',
        /*beforeSend: function(){
            document.getElementById('spinner').style.display = 'block';
        },*/
         success : function(result) {
         	modifiedPriceDtls = result;
//          console.log(modifiedPriceDtls);
         },
         async : false,
    });
    
    var result = {modifiedPriceDtls : modifiedPriceDtls,
    			  m_age : m_age,
    			  r_age : r_age};
    
    return result;
}

function produceSimulationSummary(modifiedPriceDtls,m_age,r_age){
	document.getElementById("targetRet").innerHTML = (modifiedPriceDtls.targetReturn * 100).toFixed(1) + ' %';
    document.getElementById("expShortfall").innerHTML = parseInt(modifiedPriceDtls.expectedShortfall);
    document.getElementById("shortfallLProbability").innerHTML = modifiedPriceDtls.shortfallProbability + ' %';
    document.getElementById("expVolatility").innerHTML = (modifiedPriceDtls.targetVolatility * 100).toFixed(2) + ' %';
    document.getElementById("avgResWealth").innerHTML = (modifiedPriceDtls.avgAssetValue / 1000000).toFixed(2);
    document.getElementById("yearsOfEarnings").innerHTML = parseInt(m_age)+parseInt(modifiedPriceDtls.lifeExpectancy)-parseInt(r_age) + ' Years';
}

function producePortfolioTable(modifiedPriceDtls){
	const tableBody = document.getElementById("tickerTableBody");
    var rowCount = tableBody.rows.length;
    for (var i = 0; i < rowCount; i++) {
    	tableBody.deleteRow(0);
    }
    const sortable = Object.entries(modifiedPriceDtls.portfolioFixedWtOutput).sort(([,a],[,b]) => b-a).reduce((r, [k, v]) => ({ ...r, [k]: v }), {});
    for(const key in sortable){
    	var row = tableBody.insertRow();
        var ticker = row.insertCell(0);
        ticker.innerHTML = '<a href="/company/'+key+'">'+key+'</a>';
        var percentage = row.insertCell(1);
        percentage.innerHTML = (modifiedPriceDtls.portfolioFixedWtOutput[key]*100).toFixed(2) + ' %';
    }
    
    return sortable;
}

function getAgeArray(modifiedPriceDtls,m_age){
	var ageLimit = modifiedPriceDtls.assetValuePerSimulationList[0].length;                	
    m_age = parseInt(m_age);
    var max_age = ageLimit+m_age;                   	                   	
    var ageArray = [];
    for(var k = m_age; k < max_age;k++){
    	ageArray.push(k);
    }
    
    return ageArray;
}

function getPieDataArray(modifiedPriceDtls,sortable){
	 var pieDataArray = [];
     for (const key in sortable){
     	var dataObj = {'name' : key,'y' : modifiedPriceDtls.portfolioFixedWtOutput[key]*100};
        pieDataArray.push(dataObj);
     }
     
     return pieDataArray; 
}

function populateAllSelectBox(){
	populateSelectBox(1200000,"b_expected_income");
    populateSelectBox(100000,"b_initial_investment");
    populateSelectBox(100000,"b_yearly_investment");
    populateSelectBox(100000,"c_initial_investment");
    populateSelectBox(100000,"shortTerm");
    populateSelectBox(100000,"midTerm");
    populateSelectBox(1200000,"longTerm");
    populateSelectBox(100000,"cwAmount");
}