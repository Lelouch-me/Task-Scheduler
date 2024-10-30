var baseAPI = "/api/chartData/";

function getIndexChartDataAPI(index) {
	return baseAPI+"index/"+index; 
}

function getSectorChartDataAPI(type) {
	return baseAPI+type; 
}

function getCompanyChartDataAPI(ticker) {
	return baseAPI+"company/"+ticker; 
}

	// get included header
        fetch('header')
	        .then(response => {
	            if (!response.ok) {
	                throw new Error('Network response was not ok');
	            }
	            return response.text();
	        })
	        .then(content => {
	            document.getElementById('includedContent').innerHTML = content;
	        })
	        .catch(error => {
	            console.error('Error loading included HTML:', error);
	        });

/*$(document).ready(function(){
            $.get(
                "https://www.googleapis.com/youtube/v3/search",{
                    part: 'snippet',
                    channelId: 'UCeDG4yd51RD004WqUpKTmxg',
                    type: 'video',
                    maxResults: 3,
                    order: 'date',
                key: 'AIzaSyAsdM0lFZ6aw9xLTqwgzGHP8tL1_JOUYKk'},
                function(data){
                    $.each(data.items, function(i, item){
                        console.log(item);
                        output = '<div class=\"col-lg-4 col-md-6\"> <div class=\"stock-video-panel\"> <div class=\"video-container\">'+
                            '<iframe width=\"100%\" height=\"220\" src=\"https://www.youtube.com/embed/' + item.id.videoId+'\" title=\"YouTube video player\"'+
                            ' frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>'+
                            '</div></div></div>';
                        	
                        $('#videos').append(output);	
                    })
                }
            );
        });*/