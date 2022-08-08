def servers = ["carbon-stg"];

def i = 0;

servers.each{ value -> 
  pipelineJob("QA-Selenium/Jenkins Testing/ONO/Front End/FE Smoke Tests - Staging/UIUX - BuyersGuide - Car Match - LG5-5565") {
  definition {
    cps {
      script('''
// Variables to hold config, so provided values are favored, as opposed to blindly pull from config file
def defaultValue
def server
def branch
def component
def testCase
def pageslug
def pagetype
def urlfile
def amp
def make
def model
def year
def bodystyle
def sleep
def checkoutTimeout
def buildTimeout
def testTimeout
def loglevel

def getFolderName() {
    def array = pwd().split("/")
    return array[array.length - 2];
}

node {
    configFileProvider(
        [
            configFile(
                fileId: '306ff9c4-2d17-4e8e-b3dd-dc2db1c2fa56',
                replaceTokens: true,
                targetLocation: '/Users/pablo.ramos/.jenkins/workspace/QA-Selenium/Jenkins/configfiles/configfiles/FE Core STG Configuration',
                variable: 'configData'
            )
        ]
    ) {
        load "/Users/pablo.ramos/.jenkins/workspace/QA-Selenium/Jenkins/configfiles/configfiles/FE Core STG Configuration"
        defaultValue = "${DEFAULT_VALUE}"
    }
}
node('qa-automation-npm') {
    properties(
        [
            [
                $class: 'RebuildSettings', autoRebuild: false, rebuildDisabled: false
            ], 
            parameters(
                [
                    string(defaultValue: 'CONFIG', description: '', name: 'SERVER', trim: false),
                    string(defaultValue: 'CONFIG', description: '', name: 'BRANCH', trim: false),
                    string(defaultValue: 'MT/BuyersGuide/UIUX/', description: '', name: 'COMPONENT', trim: false),
                    string(defaultValue: 'SmoketestBGCarMatchPage', description: '', name: 'TESTCASE', trim: false),
                    string(defaultValue: 'CONFIG', description: '', name: 'PAGESLUG', trim: false),
                    string(defaultValue: 'CONFIG', description: '', name: 'PAGETYPE', trim: false),
                    string(defaultValue: 'CONFIG', description: '', name: 'URLFILE', trim: false),
                    string(defaultValue: 'CONFIG', description: '', name: 'AMP', trim: false),
                    string(defaultValue: 'CONFIG', description: '', name: 'MAKE', trim: false),
                    string(defaultValue: 'CONFIG', description: '', name: 'MODEL', trim: false),
                    string(defaultValue: 'CONFIG', description: '', name: 'YEAR', trim: false),
                    string(defaultValue: 'CONFIG', description: '', name: 'BODYSTYLE', trim: false),
                    string(defaultValue: '3', description: '', name: 'SLEEP', trim: false),
                    string(defaultValue: '60', description: '', name: 'CHECKOUT_TIMEOUT', trim: false),
                    string(defaultValue: '180', description: '', name: 'BUILD_TIMEOUT', trim: false),
                    string(defaultValue: '60', description: '', name: 'TEST_TIMEOUT', trim: false),
                    string(defaultValue: 'CONFIG', description: '', name: 'LOGLEVEL', trim: false)
                ]
            ), 
            [
                $class: 'JobLocalConfiguration', changeReasonComment: ''
            ]
        ]
    )

    stage('Load configuration') {
        if ("${SERVER}" == defaultValue) {
            server = "${DEFAULT_SERVER}"
        } else {
            server = "${SERVER}"
        }

        if ("${BRANCH}" == defaultValue) {
            branch = "${DEFAULT_BRANCH}"
        } else {
            branch = "${BRANCH}"
        }
        
        if ("${COMPONENT}" == defaultValue) {
            component = "${DEFAULT_COMPONENT}"
        } else {
            component = "${COMPONENT}"
        }
        
        if ("${TESTCASE}" == defaultValue) {
            testCase = "${DEFAULT_TESTCASE}"
        } else {
            testCase = "${TESTCASE}"
        }

        if ("${PAGESLUG}" == defaultValue) {
            pageslug = "${DEFAULT_PAGESLUG}"
        } else {
            pageslug = "${PAGESLUG}"
        }

        if ("${PAGETYPE}" == defaultValue) {
            pagetype = "${DEFAULT_PAGETYPE}"
        } else {
            pagetype = "${PAGETYPE}"
        }
        
        if ("${URLFILE}" == defaultValue) {
            urlfile = "${DEFAULT_URLFILE}"
        } else {
            urlfile = "${URLFILE}"
        }
        
        if ("${AMP}" == defaultValue) {
            amp = "${DEFAULT_AMP}"
        } else {
            amp = "${AMP}"
        }
        
        if ("${MAKE}" == defaultValue) {
            make = "${DEFAULT_MAKE}"
        } else {
            make = "${MAKE}"
        }
        
        if ("${MODEL}" == defaultValue) {
            model = "${DEFAULT_MODEL}"
        } else {
            model = "${MODEL}"
        }
        
        if ("${YEAR}" == defaultValue) {
            year = "${DEFAULT_YEAR}"
        } else {
            year = "${YEAR}"
        }
        
        if ("${BODYSTYLE}" == defaultValue) {
            bodystyle = "${DEFAULT_BODYSTYLE}"
        } else {
            bodystyle = "${BODYSTYLE}"
        }
        
        if ("${SLEEP}" == defaultValue) {
            sleep = "${DEFAULT_SLEEP}"
        } else {
            sleep = "${SLEEP}"
        }
        
        if ("${CHECKOUT_TIMEOUT}" == defaultValue) {
            checkoutTimeout = "${DEFAULT_CHECKOUT_TIMEOUT}"
        } else {
            checkoutTimeout = "${CHECKOUT_TIMEOUT}"
        }
        
        if ("${BUILD_TIMEOUT}" == defaultValue) {
            buildTimeout = "${DEFAULT_BUILD_TIMEOUT}"
        } else {
            buildTimeout = "${BUILD_TIMEOUT}"
        }

        if ("${TEST_TIMEOUT}" == defaultValue) {
            testTimeout = "${DEFAULT_CHECKOUT_TIMEOUT}"
        } else {
            testTimeout = "${TEST_TIMEOUT}"
        }
        
        if ("${LOGLEVEL}" == defaultValue) {
            loglevel = "${DEFAULT_LOGLEVEL}"
        } else {
            loglevel = "${LOGLEVEL}"
        }
        
        echo "Server: " + server
        echo "Branch: " + branch
        echo "Component: " + component
        echo "Test Case: " + testCase
        echo "Urlfile: " + urlfile
        echo "Amp: " + amp
        echo "Make: " + make
        echo "Model: " + model
        echo "Year: " + year
        echo "BodyStyle: " + bodystyle
        echo "Pageslug: " + pageslug
        echo "Page Type: " + pagetype
        echo "Sleep: " + sleep
        echo "Checkout timeout : " + checkoutTimeout
        echo "Build timeout: " + buildTimeout
        echo "Test timeout: " + testTimeout
        echo "Log level: " + loglevel
        
    }
    
    stage('Checkout')  {
        retry(3) {
            try {
                echo "Initiating checkout phase."
                timeout(time: checkoutTimeout as Integer, unit: 'SECONDS') {
                    git branch: branch,
                        credentialsId: 'github',
                        url: 'git@github.com:motortrend/motortrend-lithium-web-automation.git'
                }
            } 
            catch (err) {
                error "Checkout did not complete in " + checkoutTimeout + " seconds."
            }
        }
    }
    
    stage('Build'){
        
        container ('qa-automation-npm'){
            environment {
                PUPPETEER_SKIP_CHROMIUM_DOWNLOAD=true
            }
            retry (3) {
                echo "1"
                try {
                    echo "1.5"
                    // sleep(time:3,unit:"SECONDS")
                    echo "2"
                    timeout(time: buildTimeout as Integer, unit: 'SECONDS') {
                        echo "3"
                        sh 'npm install --omit=dev --loglevel ' + loglevel
                        echo "4"
                    }
                }
                catch (err) {
                    error "Build did not complete in " + buildTimeout + " seconds"
                }
                echo "5"
            }
            
            catchError(buildResult: 'FAILURE', stageResult: 'SUCCESS') {
                echo "6"
                stage("Run Test") {
                    echo "7"
                    echo "COMPONENT=" + component + " TESTCASE=" + testCase + " SERVER=" + server + " URLFILE=" + urlfile + " AMP=" + amp + " PAGESLUG=" + pageslug + " PAGETYPE=" + pagetype +  " MAKE=" + make +   " MODEL=" + model + " YEAR=" + year + " BODYSTYLE=" + bodystyle + " npm run devtools-kubernetes"
                    retry (3) {
                        try {
                            echo "8"
                            // sleep(time:3,unit:"SECONDS")
                            sh "COMPONENT=" + component + " TESTCASE=" + testCase + " SERVER=" + server + " URLFILE=" + urlfile + " AMP=" + amp + " PAGESLUG=" + pageslug + " PAGETYPE=" + pagetype +  " MAKE=" + make +   " MODEL=" + model + " YEAR=" + year + " BODYSTYLE=" + bodystyle + " npm run devtools-kubernetes"
                            echo "9"
                        }
                        catch (err) {
                            echo "10"
                            error "Test failed: " + err
                        }
                    }   
                }
            }           
        }
    }
}
    ''')
    }
  }
  }
  i = i + 1;
}
