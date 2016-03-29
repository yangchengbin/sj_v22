(function(){
	
	//get goodsId from url
    var curWwwPath = window.document.location.href;
    var goodsId = curWwwPath.split("=")[1];
    
    //create module
	var sjShare = angular.module('sj_share',[]);
	
	//create products controller
	sjShare.controller("sjProductsCtrl", function($scope, $http){
		
		//get data from server by goodsId
		$http.get(getRootPath() + 'goods/queryGoodsDetailInfo.do', {params : {"goodsId":goodsId}}).success(function(productsData) {
			console.log(productsData);
			
			//package html for banner
			var liStr="",swipeStr="";
			if(productsData.data.desc_video){
				swipeStr += "<div><video id='desc_video' width='100%' autoplay='autoplay' loop='loop'><source src='"+productsData.data.desc_video+"' type='video/mp4'></video></div>";
				liStr += "<li class='cur'></li>";
			}
			$("#swipeWrap").append( function (){
				var swipeStr = "";
				for (var i = 0; i < productsData.imgs.length; i++) {
					swipeStr += "<div><img src='"+ productsData.imgs[i].img +"'/></div>";
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
			
			//package product for binding 
			$scope.product = {
				price : productsData.data.price,
				title : productsData.data.title,
				description : productsData.data.description,
				note : productsData.data.note,
				personage_id : productsData.data.personage_id,
				personage_name : productsData.data.personage_name,
				personage_career : productsData.data.personage_career,
				personage_head_img : productsData.data.personage_head_img
			}
			
			//create click function for the block of 'goods from' at the page bottom 
			$scope.sharePerson = function(){
				window.location.href = getRootPath() + "share_person.html?pid=" + productsData.data.personage_id;
			}
			
			//restar banner
			banner.setup();
			
		    //set video`s height based on 320*276
			$("#mySwipe").show();
		    var div_height = $("#mySwipe").width()*276/320;
			$("#desc_video").attr("height",div_height);

		}).error(function(productsData) {
			console.log("error");
		});
		
	});
	
})();