def jobName = 'Analytics - AMP Article - News - News - LG5-4321';

def paths = ["ONO/Front End/FE Smoke Tests - Staging/"
            ,"ONO/Front End/FE Smoke Tests - Preproduction behind Akamai/"
            , "ONO/Front End/FE Smoke Tests - Production/"];
def servers = ["carbon-stg"
              , "carbon-preprod-akamai"
              , "prod"];

def i = 0;

def component = "MT/AMPArticles/ANALYTICS/News/";
def testcase = "SmokeTestMTNewsAMPArticle";

servers.each{ value -> 
  pipelineJob(""${paths[i]}" + "${jobName}"") {
  definition {
    cps {
      script('''
        pipeline {
          agent any
          stages {
            stage('Build'){
                steps{
                    sh 'COMPONENT='''+"${component}"+''' TESTCASE='''+"${testcase}"+''' SERVER='''+"${servers[i]}"+''' npm run devtools-kubernetes'
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
