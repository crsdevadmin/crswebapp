var app = angular.module("sendEmail", []);


//Controller Part for Login Page
app.controller("sendEmailCtrl", function($scope , $http) {
	 $scope.EmailSuccessMessageDIV = false;
	 $scope.EmailErrorMessageDIV = false;
	$scope.sendEmail = function() {
		if(angular.isUndefined($scope.name) || $scope.name === null || $scope.name === ""){
			$scope.emailMsg = "Please enter the name";
			EmailErrorMessageDIV=true;
			return;
		}if(angular.isUndefined($scope.email) || $scope.email === null || $scope.email === ""){
			$scope.emailMsg = "Please enter the email";
			EmailErrorMessageDIV=true;
			return;
		}

		 var formData = {
				 'recipient' : 'cloudringsolutions@gmail.com',
				 'msgBody' : $scope.message,
				 'subject' : $scope.name+"-"+$scope.contact+"-"+$scope.email
				 
		 };
		 $http({
             method : 'POST',
             url : 'https://localhost:8080/sendMail',
             headers: {'Content-Type' : 'application/json'},
             data: formData
         }).then(function successCallback(response) {
        	 var responsepac = JSON.parse(JSON.stringify(response.data));
        	 $scope.status = responsepac.success;
        	 if($scope.status === true){
        		 window.location.href = '/home.html';
        	 }else{
        		 $scope.emailMsg = responsepac.exception.message;	 
        	 }
         	 
        	 
         }, function errorCallback(response) {
        	 $scope.emailMsg = 'no response from backend';
        	 EmailErrorMessageDIV=true;
         });
		
	};
});

