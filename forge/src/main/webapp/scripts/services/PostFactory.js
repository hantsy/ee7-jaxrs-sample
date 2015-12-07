angular.module('angularjsee7sample').factory('PostResource', function($resource){
    var resource = $resource('rest/posts/:PostId',{PostId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});