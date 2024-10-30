
$(window).on("load", function () {
  // Animate loader off screen
  $(".se-pre-con").fadeOut("slow");
});

// News Tab
$(".tab_content").hide();
$(".tab_content:first").show();
$("ul.tabs li").click(function () {
  $(".tab_content").hide();
  var activeTab = $(this).attr("rel");
  $("#" + activeTab).fadeIn();
  $("ul.tabs li").removeClass("active");
  $(this).addClass("active");
  $(".tab_drawer_heading").removeClass("d_active");
  $(".tab_drawer_heading[rel^='" + activeTab + "']").addClass("d_active");
});
$(".tab_drawer_heading").click(function () {
  $(".tab_content").hide();
  var d_activeTab = $(this).attr("rel");
  $("#" + d_activeTab).fadeIn();
  $(".tab_drawer_heading").removeClass("d_active");
  $(this).addClass("d_active");
  $("ul.tabs li").removeClass("active");
  $("ul.tabs li[rel^='" + d_activeTab + "']").addClass("active");
});

// News Tab Slider
$('.news-tab-slider').slick({
    dots: false,
    infinite: true,
    autoplay:true,
    speed: 800,
    slidesToShow: 4,
    slidesToScroll: 4,
    responsive: [
      {
        breakpoint: 1024,
        settings: {
          slidesToShow: 3,
          slidesToScroll: 3,
          infinite: true
        }
      },
      {
        breakpoint: 767,
        settings: {
          slidesToShow: 2,
          slidesToScroll: 2
        }
      },
      {
        breakpoint: 480,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1
        }
      }
    ]
  });

  /*$('a[data-toggle="tab"]').on('.news-tab-slider', function (e) {
    $('.news-tab-slider').slick('setPosition');
  })*/

  // MegaMenu
  $('.open-megameu').click(function(){
    $('body').addClass('open-menu-full');
  });
  $('.close-mega-menu').click(function(){
    $('body').removeClass('open-menu-full');
  })

// News Ticker
$('.my-news-ticker').AcmeTicker({
  type:'marquee',
  direction: 'left',
  speed: 0.04,
});

// Stock Ticker
$("#demo").endlessRiver({
  speed: 300,
  pause: true,
  buttons: true  
});

// Stock Ticker
$("#demo1").endlessRiver({
  speed: 300,
  pause: true,
  buttons: true  
});

// Market Snippet Select change value
$("#index-select")
  .change(function () {
    $(this)
      .find("option:selected")
      .each(function () {
        var optionValue = $(this).attr("value");
        if (optionValue) {
          $(".chart-select-item")
            .not("." + optionValue)
            .hide();
          $("." + optionValue).show();
        } else {
          $(".chart-select-item").hide();
        }
      });
  })
  .change();
  
// Market Tree Map Select change value
$("#treemap-select")
  .change(function () {
    $(this)
      .find("option:selected")
      .each(function () {
        var optionValue = $(this).attr("value");
        if (optionValue) {
          $(".tree-select-item")
            .not("." + optionValue)
            .hide();
          $("." + optionValue).show();
        } else {
          $(".tree-select-item").hide();
        }
      });
  })
  .change();

//table tab
$("ul.tabs-dta-tble li").click(function () {
  var tab_id = $(this).attr("data-tab");

  $("ul.tabs-dta-tble li").removeClass("current");
  $(".tab-content-dt-tble").removeClass("current");

  $(this).addClass("current");
  $("#" + tab_id).addClass("current");
});

$("ul.tabs-dta-tble2 li").click(function () {
  var tab_id = $(this).attr("data-tab");

  $("ul.tabs-dta-tble2 li").removeClass("current");
  $(".tab-content-dt-tble2").removeClass("current");

  $(this).addClass("current");
  $("#" + tab_id).addClass("current");
});

// top scroll vertical middle
var outerContent = $('.total-top-custom-scroll-br');
var innerContent = $('.total-top-custom-scroll-br > div');
outerContent.scrollLeft( (innerContent.width() - outerContent.width()) / 2);

// datatable sorting
$(
  ".stock-card-panel #exampletableSorting, .stock-card-panel #exampletableSorting2, .stock-card-panel #exampletableSorting3, .stock-card-panel #exampletableSorting4, .stock-card-panel #exampletableSorting5"
).DataTable({
  //order: [[ 0 ]],
  columnDefs: [{ type: "any-number", targets: [1, 2, 3] }],
  paging: false,
  bAutoWidth: false,
});

//********** Search top nav  **********//		
function autocomplete(inp, arr) {
  /*the autocomplete function takes two arguments,
  the text field element and an array of possible autocompleted values:*/
  var currentFocus;
  /*execute a function when someone writes in the text field:*/
  inp.addEventListener("input", function (e) {
    var a,
      b,
      i,
      val = this.value;
    /*close any already open lists of autocompleted values*/
    closeAllLists();
    if (!val) {
      return false;
    }
    currentFocus = -1;
    /*create a DIV element that will contain the items (values):*/
    a = document.createElement("DIV");
    a.setAttribute("id", this.id + "autocomplete-list");
    a.setAttribute("class", "autocomplete-items");
    /*append the DIV element as a child of the autocomplete container:*/
    this.parentNode.appendChild(a);
    /*for each item in the array...*/
    for (i = 0; i < arr.length; i++) {
      /*check if the item starts with the same letters as the text field value:*/
      if (arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {
        /*create a DIV element for each matching element:*/
        b = document.createElement("DIV");
        /*make the matching letters bold:*/
        b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
        b.innerHTML += arr[i].substr(val.length);
        /*insert a input field that will hold the current array item's value:*/
        b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
        /*execute a function when someone clicks on the item value (DIV element):*/
        b.addEventListener("click", function (e) {
          /*insert the value for the autocomplete text field:*/
          inp.value = this.getElementsByTagName("input")[0].value;
          /*close the list of autocompleted values,
              (or any other open lists of autocompleted values:*/
          closeAllLists();
        });
        a.appendChild(b);
      }
    }
  });
  /*execute a function presses a key on the keyboard:*/
  inp.addEventListener("keydown", function (e) {
    var x = document.getElementById(this.id + "autocomplete-list");
    if (x) x = x.getElementsByTagName("div");
    if (e.keyCode == 40) {
      /*If the arrow DOWN key is pressed,
        increase the currentFocus variable:*/
      currentFocus++;
      /*and and make the current item more visible:*/
      addActive(x);
    } else if (e.keyCode == 38) {
      //up
      /*If the arrow UP key is pressed,
        decrease the currentFocus variable:*/
      currentFocus--;
      /*and and make the current item more visible:*/
      addActive(x);
    } else if (e.keyCode == 13) {
      /*If the ENTER key is pressed, prevent the form from being submitted,*/
      e.preventDefault();
      if (currentFocus > -1) {
        /*and simulate a click on the "active" item:*/
        if (x) x[currentFocus].click();
      }
    }
  });
  function addActive(x) {
    /*a function to classify an item as "active":*/
    if (!x) return false;
    /*start by removing the "active" class on all items:*/
    removeActive(x);
    if (currentFocus >= x.length) currentFocus = 0;
    if (currentFocus < 0) currentFocus = x.length - 1;
    /*add class "autocomplete-active":*/
    x[currentFocus].classList.add("autocomplete-active");
  }
  function removeActive(x) {
    /*a function to remove the "active" class from all autocomplete items:*/
    for (var i = 0; i < x.length; i++) {
      x[i].classList.remove("autocomplete-active");
    }
  }
  function closeAllLists(elmnt) {
    /*close all autocomplete lists in the document,
    except the one passed as an argument:*/
    var x = document.getElementsByClassName("autocomplete-items");
    for (var i = 0; i < x.length; i++) {
      if (elmnt != x[i] && elmnt != inp) {
        x[i].parentNode.removeChild(x[i]);
      }
    }
  }
  /*execute a function when someone clicks in the document:*/
  document.addEventListener("click", function (e) {
    closeAllLists(e.target);
  });
}

function currentDateTime(){
	var ct = new Date();
	document.getElementById("curDateTime").innerHTML = ct.getDate()+' '+ct.toLocaleString('en-us',{ month: 'short' })
	        +' '+ct.toLocaleString('en-us',{ year: 'numeric' })+',  '+(new Date()).toLocaleTimeString('en-us',{ timeZone: "Asia/Dhaka" });
};
