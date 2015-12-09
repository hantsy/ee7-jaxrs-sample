
angular.module('angularjsee7sample2').controller('NewPostController', function ($scope, $location, locationParser, flash, PostResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.post = $scope.post || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'The post was created successfully.'});
            $location.path('/Posts');
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        PostResource.save($scope.post, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Posts");
    };
});