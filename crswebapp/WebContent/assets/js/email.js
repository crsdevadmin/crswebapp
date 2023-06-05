
var app = angular.module("sendEmailApp", []);


//Controller Part for Login Page
app.controller("sendEmailCtrl", function($scope , $http) {
	 $scope.EmailSuccessMessageDIV = false;
	 $scope.EmailErrorMessageDIV = false;
	$scope.sendEmail = function() {
		if(angular.isUndefined($scope.name) || $scope.name === null || $scope.name === ""){
			$scope.emailMsg = "Please enter the name";
			$scope.EmailErrorMessageDIV=true;
			return;
		}if(angular.isUndefined($scope.email) || $scope.email === null || $scope.email === ""){
			$scope.emailMsg = "Please enter the email";
			$scope.EmailErrorMessageDIV=true;
			return;
		}

		 var formData = {
				 'recipient' : 'cloudringsolutions@gmail.com',
				 'msgBody' : $scope.message,
				 'subject' : $scope.name+"-"+$scope.contact+"-"+$scope.email
				 
		 };
		 $http({
			 method : 'POST',
             url : 'http://localhost:8080/crsbackend/sendMail',
             headers: [{'Content-Type' : 'application/json'},{'Accept' : '*/*'}],
             data: formData
         }).then(function successCallback(response) {
        		 
        	 var responsepac = JSON.parse(JSON.stringify(response.data));
        		 
        	 $scope.status = responsepac.success;
        		 
        	 if($scope.status === true){
        		 $scope.EmailErrorMessageDIV=false;
        		 $scope.EmailSuccessMessageDIV = true;
				 $scope.emailMsg = responsepac.data;
        	 }else{
        		 $scope.emailMsg = responsepac.exception.message;	 
        	 }
         	 
        	 
         }, function errorCallback(response) {
			 console.log(response.data);
        	 $scope.emailMsg = 'no response from backend';
        	 EmailErrorMessageDIV=true;
         });
		
	};
});

