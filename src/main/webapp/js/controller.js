(function() {
    var as = angular.module('exampleApp.controllers', []);

    as.controller('MainController', function($q, $scope, $rootScope, $http, i18n, $location) {
        var load = function() {
           console.log('loading...');
        };

        load();

        $scope.language = function() {
            return i18n.language;
        };
        $scope.setLanguage = function(lang) {
            i18n.setLanguage(lang);
        };
        $scope.activeWhen = function(value) {
            return value ? 'active' : '';
        };

        $scope.path = function() {
            return $location.url();
        };

        $scope.logout = function() {
            $rootScope.user = null;
            $scope.username = $scope.password = null;
            $scope.$emit('event:logoutRequest');
            $location.url('/login');
        };
        
    });

    as.controller('LoginController', function($scope, $rootScope, $http, base64, $location) {

        $scope.login = function() {
            console.log('username:password @' + $scope.username + ',' + $scope.password);
            $scope.$emit('event:loginRequest', $scope.username, $scope.password);
            // $('#login').modal('hide');
        };
    });
    
    as.controller('MembersController', function($scope, $rootScope, $http, base64, $location, Members){
    		

	    var actionUrl = 'rest/members',
                load = function() {
                    $http.get(actionUrl).success(function(data) {
                        $scope.members = data;
                    });
                };

        load();

        $scope.member={};
    
	    $scope.del = function(id) {
            $http.delete(actionUrl+'/'+ id)
            .success(function() {
                load();
            });
        };
               
	
	    $scope.order = 'name';
	    
		$scope.orderBy = function(property) {
		
		    $scope.order = ($scope.order[0] === '+' ? '-' : '+') + property;
		};
		
		$scope.orderIcon = function(property) {
		    return property === $scope.order.substring(1) ? $scope.order[0] === '+' ? 'glyphicon glyphicon-chevron-up' : 'glyphicon glyphicon-chevron-down' : '';
		};
		
    
	    $scope.newMember=function(){
	    	$scope.member= {};
	    	$('#memberDialog').modal('show');
	    }

	    $scope.editMember=function(idx){
	    	console.log('selected index @'+idx);
	    	$scope.member= $scope.members[idx];
	    	$('#memberDialog').modal('show');
	    }
	    
	    $scope.reset=function(){
	    	$scope.member={};
	    	$('#memberDialog').modal('hide');
	    }

	    $scope.save=function(){
	    	$scope.successMessages = '';
		    $scope.errorMessages = '';
		    $scope.errors = {};
		    
	    	if(!!$scope.member.id){
		    	$http.put(actionUrl+'/'+$scope.member.id, $scope.member)
		    		.success(function(data, status){
		    			// mark success on the registration form
			            $scope.successMessages = [ 'Member Registered' ];
			            $('#memberDialog').modal('hide');
			            load();
		    		})
		    		.error(function(data, status){
		    			 if ((status == 409) || (status == 400)) {
		 	                $scope.errors = data;
		 	            } else {
		 	                $scope.errorMessages = [ 'Unknown  server error' ];
		 	            }
		    		});
	 	     }else{
	 	    	 
	 	          $http.post(actionUrl, $scope.member)
	 	    	        .success(function(data, status) {
	 	    	
	 	    	            // mark success on the registration form
	 	    	            $scope.successMessages = [ 'Member Registered' ];
	 	    	            $('#memberDialog').modal('hide');
	 	    	            load();
	 	    	        })
	 	    	        .error(function(data, status) {
	 	    	            if ((status == 409) || (status == 400)) {
	 	    	                $scope.errors = data;
	 	    	            } else {
	 	    	                $scope.errorMessages = [ 'Unknown  server error' ];
	 	    	            }
	 	    	           
	 	    	        });
	 	      }
	    
	    }
    });

}());