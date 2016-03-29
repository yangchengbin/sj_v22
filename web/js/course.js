(function () {

    //get courseId from url
    var curWwwPath = window.document.location.href;
    var courseId = curWwwPath.split("=")[1];
    
    //create module
	var sjShare = angular.module('sj_share',[]);
	
	//create products controller
	sjShare.controller("sjCourseCtrl", function($scope, $http){
		
		//get data from server by courseId
		$http.get(getRootPath() + 'course/queryCourseDetailInfo.do', {params : {"courseId":courseId}}).success(function(courseData) {
			console.log(courseData);
			
			//package html for banner
			var liStr="",swipeStr="";
			if(courseData.data.desc_video){
				swipeStr += "<div><video id='desc_video' width='100%' autoplay='autoplay' loop='loop'><source src='"+courseData.data.desc_video+"' type='video/mp4'></video></div>";
				liStr += "<li class='cur'></li>";
			}
			$("#swipeWrap").append(function () {
		        var swipeStr = "";
		        for (var i = 0; i < courseData.imgs.length; i++) {
		            swipeStr += "<div><img src='" + courseData.imgs[i].img + "'/></div>";
		            if(liStr == ""){
			            liStr += "<li class='cur'></li>";
		            }else{
		            	liStr += "<li class=''></li>";
		            }
		        }
		        return swipeStr;
		    });
		    $("#position").append(liStr);
			
		    //play pause video
		    $("#desc_video").click(function(){
		    	if($(this)[0].paused){
		    		$(this)[0].play();
		    	}else{
		    		$(this)[0].pause();
		    	}
		    });
			
			//package course for binding 
			$scope.course = {
	            title: courseData.data.title,
	            price: courseData.data.price,
	            course_time: courseData.data.course_time,
	            course_address: courseData.data.course_address,
	            description: courseData.data.description,
	            note: courseData.data.note,
	            personage_id: courseData.data.personage_id,
	            personage_name: courseData.data.personage_name,
	            personage_career: courseData.data.personage_career,
	            personage_head_img: courseData.data.personage_head_img
	        };
			
			//create click function for the block of 'course from' at the page bottom 
			$scope.sharePerson = function(){
				window.location.href = getRootPath() + "share_person.html?pid=" + courseData.data.personage_id;
			}
			
			//restar banner
			banner.setup();
			
		    //reset video`s height based on 320*276
			$("#mySwipe").show();
		    var div_height = $("#mySwipe").width()*276/320;
			$("#desc_video").attr("height",div_height);

		}).error(function(productsData) {
			console.log("error");
		});
		
	});

})();