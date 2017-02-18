'use strict';

App.factory('UserService', ['$http', '$q', function($http, $q){

    var HOST = "/user";

    return {
        newUser: function() {
            return $http.get(HOST + '/new')
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while create new user');
                        return $q.reject(errResponse);
                    }
                );
        }
    };

}]);
