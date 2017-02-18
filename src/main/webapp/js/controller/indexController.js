'use strict';

App.controller('indexController', ['$scope', 'UserService', 'TaskService', '$interval', function($scope, UserService, TaskService, $interval) {

    $scope.userId = '';

    $scope.pageSize = 7;
    $scope.currentPage = 1;

    $scope.totalItems = 1;
    $scope.totalPages = 1;
    $scope.tasks = [];
    
    var newUser = function(){
        UserService.newUser()
            .then(
                function(d) {
                    $scope.userId = d.id;
                    console.log("New user with id: " + $scope.userId);
                },
                function(error){
                    console.log("Error init new user");
                }
            );
    };

    $scope.newTask = function (task) {
        task.userId = $scope.userId;
        TaskService.createTask(task).then(function (results) {
            console.log("New task with src: " + task.src + "algo: " + task.algo);
            $scope.pageChanged();
        }, function (error) {
            console.log(error.message);
        });
    };

    var loadPages = function () {
        TaskService.loadAllTasks($scope.currentPage-1, $scope.pageSize, $scope.userId).then(function (results) {
            console.log(results);

            $scope.totalItems = results.totalElements;
            $scope.totalPages = results.totalPages;

            $scope.tasks = results.content;

        }, function (error) {
            console.log(error.message);
        });
    };

    $scope.deleteTask = function(taskId) {
        console.log($scope.selection);
        TaskService.deleteTask(taskId, $scope.userId).then(function (results) {
            console.log(results);
            console.log('deleted');
            $scope.pageChanged();
        }, function (error) {
            console.log(error.message);
        });
    };

    $scope.stopTask = function(taskId) {
        console.log($scope.selection);
        TaskService.stopTask(taskId, $scope.userId).then(function (results) {
            console.log(results);
            console.log('stopped');
            $scope.pageChanged();
        }, function (error) {
            console.log(error.message);
        });
    };

    newUser();
    loadPages();

    $scope.setPage = function (pageNo) {
        $scope.currentPage = pageNo;
    };

    $scope.pageChanged = function () {
        loadPages();
    };

    $scope.reloadRoute = function () {
        $route.reload();
    };

    $interval(function(){
        $scope.pageChanged();
    }.bind(this), 2000);

}]);
