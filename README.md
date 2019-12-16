
# cds-imports-dds-frontend

This is a placeholder README.md for a new repository.

### Autocomplete scripts

This project has TamperMonkey scripts available in user-scripts directory.

#### TamperMonkey
[Chrome Extension](https://chrome.google.com/webstore/detail/tampermonkey/dhdgffkkebhmkfjojejmpbldmpobfkfo?hl=en)<br>
[Firefox Extension](https://addons.mozilla.org/pl/firefox/addon/tampermonkey/)


### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

# Installation   

### Cloning:
SSH
```
git@github.com:hmrc/cds-imports-dds-frontend.git
```
HTTPS
```
https://github.com/hmrc/cds-imports-dds-frontend.git
```
### Running the application

After starting your local MongoDB instance on port 27017 and opening HMRC VPN, ensure that you have the latest versions of the required services and that they are running. This can be done via service manager using the CDS_IMPORTS_DDS_ALL profile
```
sm --start CDS_IMPORTS_DDS_ALL -f

* In your browser navigate to http://localhost:9949/auth-login-stub/gg-sign-in
* In the Redirect URL field enter http://localhost:9760/customs/imports
* In the Enrolment Key field enter HMRC-CUS-ORG
* In the Identifier Name field enter EORINumber
* In the Identifier Value field enter GB744638982000
```

## Build

* `cd` to the root of the project.

```shell
$ sbt clean compile
```

## Unit Tests

### Unit Test Build-only

```shell
$ sbt test:compile
```

### Running Unit Tests

```shell
$ sbt test
```

### Running Unit Test Coverage report
sbt clean coverage test coverageReport

### Running acceptance tests locally

 ./run_local_acceptance.sh

## External Test environment
The service is deployed and running in the external test environment.
Declarations are accepted by DMS in this environment.

```
 https://test-www.tax.service.gov.uk/auth-login-stub/gg-sign-in
```
