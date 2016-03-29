(function () {

	//get personId from url
    var curWwwPath = window.document.location.href;
    var personId = curWwwPath.split("=")[1];

    //create module
	var sjShare = angular.module('sj_share',[]);

	//create products controller
	sjShare.controller("sjPersonCtrl", function($scope, $http){

		//get data from server by personId
		$http.get(getRootPath() + 'personage/queryPersonInfo.do', {params : {"personageId":personId}}).success(function(personData) {
			console.log(personData);

			//page html for video banner 
			var liStr="",swipeStr="";
			if(personData.data.desc_video){
				//swipeStr += "<div><img style='position:fixed;width:20%;left:40%;top:40%;' src='http://img.ylznet.com/src_p/sj_pc/img/play.png' id='poster' /><img style='position:fixed;width:20%;left:40%;top:40%;z-index:4;' src='http://img.videocc.net/uimage/4/479c25d625/7/479c25d6253a63fea832958caa4a99f7_0.jpg' /><video id='desc_video' width='100%' autoplay='autoplay' loop='loop'><source src='"+personData.data.desc_video+"' type='video/mp4'></video></div>";
				swipeStr += "<div><img style='position:fixed;width:20%;left:40%;top:40%;' src='http://img.ylznet.com/src_p/sj_pc/img/play.png' id='poster' /><video id='desc_video' width='100%' autoplay='autoplay' loop='loop'><source src='"+personData.data.desc_video+"' type='video/mp4'></video></div>";
				//swipeStr += "<div><video poster='http://img.videocc.net/uimage/4/479c25d625/c/479c25d6257002059e8356d8a62aa74c_0.jpg' id='desc_video' width='100%' autoplay='autoplay' loop='loop'><source src='"+personData.data.desc_video+"' type='video/mp4'></video></div>";
				liStr += "<li class='cur'></li>";
			}
			$("#swipeWrap").append(function () {
	        	for (var i = 0; i < personData.imgs.length; i++) {
		            swipeStr += "<div><img src='" + personData.imgs[i].img + "'/></div>";
		            liStr += "<li class=''></li>";
		        }
		        return swipeStr;
		    });
		    $("#position").append(liStr);

		    //play pause video
		    $("#desc_video").click(function(){
		    	if($(this)[0].paused){
		    		$(this)[0].play();
		    		if($("#poster")){$("#poster").fadeOut(100)};
		    	}else{
		    		$(this)[0].pause();
		    		if($("#poster")){$("#poster").fadeIn(100)};
		    	}
		    });

			//package person for binding 
			$scope.person = {
	            head_img: personData.data.head_img,
	            pname: personData.data.pname,
	            career: personData.data.career,
	            recommendation: personData.data.recommendation,
	            description : personData.data.description
	        }

			//restar banner,necessary
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
