'use strict';

App.factory('TaskService', ['$http', '$q', function($http, $q){

    var HOST = "http://localhost:8080/tasks";

    return {

        loadAllTasks: function(page, count, userId) {
            return $http.get(HOST + '?page=' + page + '&count=' + count + '&userId=' + userId)
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while load tasks');
                        return $q.reject(errResponse);
                    }
                );
        },

        createTask: function(task){
            return $http.post(HOST + '/add', task)
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while creating task');
                        return $q.reject(errResponse);
                    }
                );
        },

        stopTask: function(taskId, userId){
            return $http.get(HOST + '/stop?taskId=' + taskId + '&userId=' + userId)
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while updating task');
                        return $q.reject(errResponse);
                    }
                );
        },

        deleteTask: function(taskId, userId){
            return $http.delete(HOST +'?taskId=' + taskId + '&userId=' + userId)
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while deleting task');
                        return $q.reject(errResponse);
                    }
                );
        }

    };

}]);
