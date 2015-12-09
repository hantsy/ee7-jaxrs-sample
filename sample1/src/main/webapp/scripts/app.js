'use strict';

angular.module('angularjsee7sample',['ngRoute','ngResource'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/',{templateUrl:'views/landing.html',controller:'LandingPageController'})
      .when('/Posts',{templateUrl:'views/Post/search.html',controller:'SearchPostController'})
      .when('/Posts/new',{templateUrl:'views/Post/detail.html',controller:'NewPostController'})
      .when('/Posts/edit/:PostId',{templateUrl:'views/Post/detail.html',controller:'EditPostController'})
      .otherwise({
        redirectTo: '/'
      });
  }])
  .controller('LandingPageController', function LandingPageController() {
  })
  .controller('NavController', function NavController($scope, $location) {
    $scope.matchesRoute = function(route) {
        var path = $location.path();
        return (path === ("/" + route) || path.indexOf("/" + route + "/") == 0);
    };
  });
