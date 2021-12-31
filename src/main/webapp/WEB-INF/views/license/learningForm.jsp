<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--import spring suppiled JSP tag lib for URL rewriting --%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%--import , for the form binding tech , spring supplied form tag lib  --%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<title>Apply For Learner's License</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</head>
<body>

	<div class="container-fluid p-3 my-3 bg-dark text-white">
		<h2 style="text-align: center;">e-RTO & Operations</h2>
		<p style="text-align: center;">Welcome to the e-RTO &
			Operations.The place where transparency is the main moto.</p>
	</div>



	<nav
		class="navbar navbar-expand-sm bg-dark navbar-dark justify-content-center">
		<ul class="navbar-nav mr-auto">
			<li class="nav-item"><a class="nav-link"
				href="<spring:url value='/user/page'/>">Home </a></li>

			<li class="nav-item dropdown"><a
				class="nav-link dropdown-toggle active" href="#" id="navbardrop"
				data-toggle="dropdown"> License </a>
				<div class="dropdown-menu">
					<a class="dropdown-item"
						href="<spring:url value='/license/learning'/>">Learning
						License </a> <a class="dropdown-item"
						href="<spring:url value='/license/permanent'/>">Permanent
						License </a>
				</div></li>

			<li class="nav-item "><a class="nav-link"
				href="<spring:url value='/user/mocktest'/>">Mock Test </a></li>
				
				<li class="nav-item "><a class="nav-link"
				href="<spring:url value='/user/status'/>">Status </a></li>

		</ul>

		<ul class="navbar-nav ml-auto">
			<li class="nav-item"><a class="nav-link"
				href="<spring:url value='/user/logout'/>">
					<button type="button" class="btn btn-info btn-sm">Log Out</button>
			</a></li>

		</ul>
	</nav>


	<hr />
	<div class="row">
		<div class="col"></div>

		<div class="col">

			<div class="card bg-light " style="width: 600px; height: 1150px">
				<div class="card-header" style="text-align: center;">
					<h5>Apply For Learning License</h5>
				</div>
				<div class="card-body">

					<form:form method="post" modelAttribute="learningLicense">
						<div class="row">
							<div class="col">
								<div class="form-group">
									<label for="firstName">First Name</label> <input type="text"
										class="form-control" placeholder="Enter First name"
										id="firstName" name="firstName" pattern="[A-Za-z]{1,}">

								</div>
							</div>
							<div class="col">
								<div class="form-group">
									<label for="lastname">Last Name</label> <input type="text"
										class="form-control" placeholder="Enter Last name"
										id="lastName" name="lastName" pattern="[A-Za-z]{1,}">

								</div>
							</div>
						</div>

						<div class="form-group">
							<label for="email">Email Id :</label> <input type="email"
								class="form-control" placeholder="Enter email" id="email"
								name="email">


						</div>
						<div class="form-group">
							<label for="aadharNo">Aadhar No.</label> <input type="text"
								class="form-control" placeholder="Enter Aadhar No" id="aadharNo"
								name="aadharNo" pattern="[1-9]{1}[0-9]{11}">
						</div>

						<div class="form-group">
							<label for="mobileNo">Mobile No.</label> <input type="text"
								class="form-control" placeholder="Eg. +918888888888" id="mobileNo"
								name="mobileNo" pattern="^[+]91(9|8|7)\d{9}$">
						</div>

						<div class="row">
							<div class="col">
								<div class="form-group">
									<label for="dob">Choose Birth Date</label> <input type="date"
										class="form-control" id="dob" name="dob" required="required"
										onchange="ValidateDOB()" onblur="ValidateDOB()" />

									<script type="text/javascript">
										function ValidateDOB() {
											var lblError = document
													.getElementById("lblError");

											//Get the date from the TextBox.
											var dateString = document
													.getElementById("txtDate").value;
											var regex = /(((0|1)[0-9]|2[0-9]|3[0-1])\/(0[1-9]|1[0-2])\/((19|20)\d\d))$/;

											//Check whether valid dd/MM/yyyy Date Format.
											if (regex.test(dateString)) {
												var parts = dateString
														.split("-");
												var dtDOB = new Date(parts[1]
														+ "-" + parts[0] + "-"
														+ parts[2]);
												var dtCurrent = new Date();
												lblError.innerHTML = "Eligibility 18 years ONLY."
												if (dtCurrent.getFullYear()
														- dtDOB.getFullYear() < 18) {
													return false;
												}

												if (dtCurrent.getFullYear()
														- dtDOB.getFullYear() == 18) {

													//CD: 11/06/2018 and DB: 15/07/2000. Will turned 18 on 15/07/2018.
													if (dtCurrent.getMonth() < dtDOB
															.getMonth()) {
														return false;
													}
													if (dtCurrent.getMonth() == dtDOB
															.getMonth()) {
														//CD: 11/06/2018 and DB: 15/06/2000. Will turned 18 on 15/06/2018.
														if (dtCurrent.getDate() < dtDOB
																.getDate()) {
															return false;
														}
													}
												}
												lblError.innerHTML = "";
												return true;
											} else {
												lblError.innerHTML = "Enter date in dd/MM/yyyy format ONLY."
												return false;
											}
										}
									</script>

								</div>
							</div>
							<div class="col">
								<div class="form-group">
									<label for="gender">Gender</label> <select class="form-control"
										id="gender" name="gender">
										<option value="">Choose...</option>
										<option value="MALE">MALE</option>
										<option value="FEMALE">FEMALE</option>
										<option value="PreferNotToSay">Prefer Not to Say</option>
										<option value="Other">Other</option>
									</select>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<div class="form-group">
									<label for="bloodGroup">Blood Group</label> <select class="form-control"
										id="bloodGroup" name="bloodGroup">
										<option value="">Choose...</option>
										<option value="A+">A+</option>
										<option value="A-">A-</option>
										<option value="B+">B+</option>
										<option value="B-">B-</option>
										<option value="O+">O+</option>
										<option value="O-">O-</option>
										<option value="AB+">AB+</option>
										<option value="AB-">AB-</option>
									</select>

								</div>
							</div>
							<div class="col">
								<div class="form-group">
									<label for="identificationMark">Identification Mark</label> <input
										type="text" class="form-control"
										placeholder="Identification Mark" id="identificationMark"
										name="identificationMark" pattern="[A-Za-z\s]{1,}">

								</div>
							</div>
						</div>

						<div class="row">
							<div class="col">
								<div class="form-group">
									<div class="form-group">
										<label for="state">State</label> <select class="form-control"
											id="state" name="state" required>
											<option value="">Choose...</option>
											<option value="Maharashtra">Maharashtra</option>
											<option value="Uttar Pradesh" disabled>Uttar Pradesh</option>
										</select>
									</div>

								</div>
							</div>
							<div class="col">
								<div class="form-group">
									<label for="district">District</label> <select class="form-control"
											id="district" name="district" required>
											<option value="">Choose...</option>
											<option value="Pune">Pune</option>
											<option value="Jalgaon" disabled>Jalgaon</option>
										</select>
								</div>
							</div>
						</div>


						<div class="row">
							<div class="col">
								<div class="form-group">
									<div class="form-group">

										<label for="pincode">PinCode</label> <input type="text"
											class="form-control" placeholder="Enter PinCode" id="pincode"
											name="pincode" pattern="[1-9]{1}[0-9]{2}\\s{0, 1}[0-9]{3}"
											maxlength="6">

									</div>

								</div>
							</div>
							<div class="col">
								<div class="form-group">
									<label for="village">Village/City</label> <input type="text"
										class="form-control" placeholder="Enter Village/City"
										id="village" name="village" pattern="[A-Za-z]{1,}">

								</div>
							</div>
						</div>



						<div class="row">
							<div class="col">
								<div class="form-group">
									<div class="form-group">

										<label for="landmark">Landmark</label> <input type="text"
											class="form-control" placeholder="Enter Landmark"
											id="landmark" name="landmark">

									</div>

								</div>
							</div>
							<div class="col">
								<div class="form-group">
									<label for="street">Street</label> <input type="text"
										class="form-control" placeholder="Enter Street" id="street"
										name="street">

								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="col">
								<div class="form-group">
									<div class="form-group">
										<label for="appointmentDate">Test Date:</label> <input
											type="date" name="appointmentDate" id="appointmentDate"
											class="form-control" />
									</div>
								</div>
							</div>
							<div class="col">
								<div class="form-group">
									<div class="form-group">
										<div class="form-group">
    									  <label for="appointmentTime">Test Time</label>
    									  <select class="form-control" id="appointmentTime" name="appointmentTime" required>
    									     <option value="" >Slot</option>
   										     
   										     <option value="11:00" >11:00 AM</option>
   										     <option value="01:30" >01:30 PM</option>
  										     <option value="03:30" >03:30 PM</option>
  										     
    										  </select>
											</div>
									</div>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label for="createdAt">Created At</label> <input type="text"
								class="form-control" id="createdAt">
							<script>
								var today = new Date();
								var date = today.getFullYear() + '-'
										+ (today.getMonth() + 1) + '-'
										+ today.getDate();
								var time = today.getHours() + ":"
										+ today.getMinutes() + ":"
										+ today.getSeconds();
								var dateTime = date + ' ' + time;
								document.getElementById("createdAt").value = dateTime;
							</script>
						</div>

						<button type="submit" class="btn btn-primary mx-auto d-block"
							value="Next">Next</button>

						<p style="text-align: center;">
							<br> Already Applied For Learning License ?<a
								href="<spring:url value='/license/permanent'/>"
								class="text-success"> Click Here to Apply for Permanent</a>
						</p>


					</form:form>

				</div>
			</div>
		</div>
		<div class="col"></div>

	</div>
	<hr />
	
	<div class ="container-fluid  pt-1 p-1 my-1 bg-dark text-white  ">
<p style="text-align:center; font-family:redressed,georgia,garamond,serif;">©Copyright-2021 e-RTO & Operations</p>
</div>
	
</body>
</html>