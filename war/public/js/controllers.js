'use strict';

function UserCtrl($scope, backend) {
    $scope.user = null;
    $scope.login = function () {
        backend.user().then(function (response) {
            $scope.user = response.data;
        });
    }
    $scope.login();
}

function EditorCtrl($scope, $location, $routeParams, $timeout, editor, doc, autosaver) {
    console.log($routeParams);
    $scope.editor = editor;
    $scope.doc = doc;
    $scope.$on('saved',
        function (event) {
            $location.path('/edit/' + doc.resource_id);
        });
    if ($routeParams.id) {
        editor.load($routeParams.id);
    } else {
        // New doc, but defer to next event cycle to ensure init
        $timeout(function () {
                editor.create();
            },
            1);
    }
}

function ShareCtrl($scope, appId, doc) {
    /*
     var client = gapi.drive.share.ShareClient(appId);
     $scope.enabled = function() {
     return doc.resource_id != null;
     };
     $scope.share = function() {
     client.setItemIds([doc.resource_id]);
     client.showSharingSettings();
     }
     */
	
}

function MenuCtrl($scope, $location, appId) {
    var onFilePicked = function (data) {
        $scope.$apply(function () {
            if (data.action == 'picked') {
                var id = data.docs[0].id;
                $location.path('/edit/' + id);
            }
        });
    };
    $scope.open = function () {
        var view = new google.picker.View(google.picker.ViewId.DOCS);
        view.setMimeTypes('text/plain');
        var picker = new google.picker.PickerBuilder()
            .setAppId(appId)
            .addView(view)
            .setCallback(angular.bind(this, onFilePicked))
            .build();
        picker.setVisible(true);
    };
    $scope.create = function () {
        this.editor.create();
    };
    $scope.save = function () {
        this.editor.save(true);
    }
}

function RenameCtrl($scope, doc) {
    $('#rename-dialog').on('show',
        function () {
            $scope.$apply(function () {
                $scope.newFileName = doc.info.title;
            });
        });
    $scope.save = function () {
        doc.info.title = $scope.newFileName;
        $('#rename-dialog').modal('hide');
    };
}

function AboutCtrl($scope, backend) {
    $('#about-dialog').on('show',
        function () {
            backend.about().then(function (result) {
                $scope.info = result.data;
            });
        });    
}

/*Functions added by Avani BEGIN here*/
function addCardCtrl($scope, backend) {
			$scope.addCreditCard = function () {
				creditCardName = $('#creditCardName').val();
				console.log("Entered Credit card is: " + creditCardName);
			backend.insertspreadsheet();
			};
			
	}
/*Functions added by Avani END here*/

/*Functions added by Juan Begin here
 * TODO: Add the code to call the drive API and actually make the changes*/
function generateReportCtrl($scope, $http) {

	$scope.generateReportByMonth = function () {
		window.alert("Coming soon: Creating a report by month");
		$('#generate-report-dialog').modal('hide');
	};

	$scope.generateReportCreditCard = function () {
		window.alert("Coming soon: Creating a report by selecting credit card");
		$('#generate-report-dialog').modal('hide');
	};
	
	$scope.generateReporSummary = function () {
		var currDate = new Date();
		var dateString = currDate.toDateString() + " " + currDate.getHours() + ":" + 
			currDate.getMinutes() + ":" + currDate.getSeconds() + ".txt";
		$http.get("/ccreport", {
			params: {
				reportType:"summary",
				selectValue:"",
				fileName:"Report by summary " + dateString 
				}
			}).then(function (response) {
				console.log ("generateReportSummary response JSON object is: " + JSON.stringify(response) );
				window.alert("The file that will be created: " + response.data.members.fileNameThatWasPassedDownWas);
			});
		//TODO: Error handling
		$('#generate-report-dialog').modal('hide');
	};
	
}

function enterPaymentCtrl($scope) {
	
	$scope.isYearValid = false;
	$scope.isPaymentAmountValid = false;
	$scope.isBalanceAmountValid = false;
	$scope.isAPRValid = false;
	$('#addPaymentButton').attr('disabled','disabled');
	
	$scope.checkIfEnableOrDisable = function () {
		console.log("isYearValid:"  + $scope.isYearValid + " payment:" +  $scope.isPaymentAmountValid +
				" balance:" + $scope.isBalanceAmountValid + " APR:" + $scope.isAPRValid);
		if($scope.isYearValid && $scope.isPaymentAmountValid && $scope.isBalanceAmountValid && $scope.isAPRValid)
			$('#addPaymentButton').removeAttr("disabled");
		else
			$('#addPaymentButton').attr('disabled','disabled');   
	} 
	
	//check to see if the year entered is valid
	$('#ccYearField').on('change', function () {  
		var valueEntered = $('#ccYearField').val();
		console.log("entered year is: " + valueEntered);
		var valueEnteredAsNumber = Number(valueEntered);
		console.log("entered year as number is:" + valueEnteredAsNumber);
		if(! valueEnteredAsNumber) { //value entered is not a NaN
			$('#ccYearEnteredErrorMsg').text("Please enter a valid year in the form YYYY");
			$scope.isYearValid = false;
			$scope.checkIfEnableOrDisable();
		}
		else if(valueEnteredAsNumber < 1900 ) {
			$('#ccYearEnteredErrorMsg').text("Year must be after 1900");
			$scope.isYearValid = false;
			$scope.checkIfEnableOrDisable();
		}
		else if(valueEnteredAsNumber > 2100 ) {
			$('#ccYearEnteredErrorMsg').text("Year must be before 2100");
			$scope.isYearValid = false;
			$scope.checkIfEnableOrDisable();
		}
		else {
			$('#ccYearEnteredErrorMsg').text("");
			$scope.isYearValid = true;
			console.log("YAY!");
			$scope.checkIfEnableOrDisable();
		}
	});
	
	//check to see if the payment amount is valid
	//TODO: Remove '$' and ',' so that numbers like $5,000 can be accepted
	$('#ccPaymentField').on('change', function () {  
		var valueEntered = $('#ccPaymentField').val();
		console.log("entered Payment is: " + valueEntered);
		var valueEnteredAsNumber = Number(valueEntered);
		console.log("entered payment as number is:" + valueEnteredAsNumber);
		if(! valueEnteredAsNumber) { //value entered is not a NaN
			$('#ccPaymentEnteredErrorMsg').text("Balance entered must be a number");
			$scope.isPaymentAmountValid = false;
			$scope.checkIfEnableOrDisable();
		}
		else if(valueEnteredAsNumber < 0 ) {
			$('#ccPaymentEnteredErrorMsg').text("Balance must be greater than 0");
			$scope.isPaymentAmountValid = false;
			$scope.checkIfEnableOrDisable();
		}
		else {
			$('#ccPaymentEnteredErrorMsg').text("");
			$scope.isPaymentAmountValid = true;
			$scope.checkIfEnableOrDisable();
		}
	});

	//check to see if balance entered is valid
	//TODO: Remove '$' and ',' so that numbers like $5,000 can be accepted
	$('#ccBalanceField').on('change', function () {  
		var valueEntered = $('#ccBalanceField').val();
		console.log("entered Balance is: " + valueEntered);
		var valueEnteredAsNumber = Number(valueEntered);
		console.log("entered balance as number is:" + valueEnteredAsNumber);
		if(! valueEnteredAsNumber) { //value entered is not a NaN
			$('#ccBalanceEnteredErrorMsg').text("Balance entered must be a number");
			$scope.isBalanceAmountValid = false;
			$scope.checkIfEnableOrDisable();
		}
		else if(valueEnteredAsNumber < 0 ) {
			$('#ccBalanceEnteredErrorMsg').text("Balance must be greater than 0");
			$scope.isBalanceAmountValid = false;
			$scope.checkIfEnableOrDisable();
		}
		else {
			$('#ccBalanceEnteredErrorMsg').text("");
			$scope.isBalanceAmountValid = true;
			console.log("isBalanceAmountValid was set to true");
			$scope.checkIfEnableOrDisable();
		}
	});

	//check to see if APR entered is valid
	//TODO: Remove '%' and ',' so that numbers like 10% can be accepted
	$('#ccAprField').on('change', function () {  
		var valueEntered = $('#ccAprField').val();
		console.log("entered APR is: " + valueEntered);
		var valueEnteredAsNumber = Number(valueEntered);
		console.log("entered APR as number is:" + valueEnteredAsNumber);
		if(! valueEnteredAsNumber) { //value entered is not a NaN
			$('#ccAprEnteredErrorMsg').text("Interest should be a number");
			$scope.isAPRValid = false;
			$scope.checkIfEnableOrDisable();
		}
		else if(valueEnteredAsNumber < 0 ) {
			$('#ccAprEnteredErrorMsg').text("Interest should be greater than 0");
			$scope.isAPRValid = false;
			$scope.checkIfEnableOrDisable();
		}
		else if(valueEnteredAsNumber > 100 ) {
			$('#ccAprEnteredErrorMsg').text("Interest should be less than 100");
			$scope.isAPRValid = false;
			$scope.checkIfEnableOrDisable();
		}
		else {
			$('#ccAprEnteredErrorMsg').text("");
			$scope.isAPRValid = true;
			$scope.checkIfEnableOrDisable();
		}
	});

	
	$scope.addPaymentRow = function () {
		//add code to add payment
	};
	
}


