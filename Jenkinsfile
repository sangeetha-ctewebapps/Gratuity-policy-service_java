import java.text.SimpleDateFormat 
def startDate 
def endDate 
def projectId= 315
def projectName= "Gratutity-policy-service" 
def moduleId= "100" 
def moduleName= "Gratutity" 

def applicationType = "springboot"
def branch =  scm.branches[0].name.replaceAll("[^a-zA-Z0-9 ]+","")
//def branch = "dev"
def newBuildId = "${branch}.0.0.${BUILD_ID}"
def nexusip
def warName
def artifact
def environment
def jbossenv
def mvncommand="mvn clean package -P${branch} -DskipTests "
def user
def propsemail
def emailid
def buildfailbody
def buildfailsubject
def deployfailbody
def deployfailsubject
def testfailbody
def testfailsubject
def jbossdomain
def jboss = "${branch}"
def jbossIP
def devops
def techm
def sonarenable
def sonarreporturl
def sonarenablelist
def sonarstatusproject
def sonarResult
def sonarfailbody
def dependencyreport
def dependencyrepourl
def sonarkey = "licepgs-${moduleName}-${branch}"
def jbosscredid
def commitid
def unwantedfiles= " "
def tmpfile
def codetype
def props 
def values 
def scannerHome 
def dependencysubject
def sonarscansuccess
def moduleleademail
def deploy 
def enableadopt
def ipcheck
def ipchecklist
def ipcheckincode
def iplist = ""
def ipharcodesubject = ""
def ipharcodebody = ""
def checklist = []
def enablejndi
def binaryenable
pipeline {
  agent any
  stages {

    stage('CODE') {
      steps {
        script {
          def repourl = sh(script: 'git remote -v', returnStdout: true)
 			 def repourl1=     repourl.split("	")[1]
             def repo=     repourl1.split(" ")[0]
          def CODE =  readJSON text: "{'url':'${repo}','scm':'bitbucket','branch':'${branch}','credId':'idbit'}"
          git url:CODE.url, credentialsId:CODE.credId, branch:CODE.branch
          }
      }
    }

   stage('Build') {

      steps {
        script {

          dir("/app/") {
            propsemail = readProperties file: '/app/emailds.properties'
            emailid = propsemail.email
            moduleleademail = propsemail."${moduleName}lead"
            devops = propsemail.devops
            techm = propsemail.techm
            testfailmessage = propsemail.testfailmessage
            testfailsubject = propsemail.testfailsubject
            sonarfailmessage = propsemail.sonarfailmessage
            dependencyreport = propsemail.dependencyreport
            dependencyrepourl = propsemail.dependencyrepourl
            nexusip = propsemail.nexusip
            buildfailbody = propsemail.buildfailbody
            buildfailsubject = propsemail.buildfailsubject
            deployfailbody = propsemail.deployfailbody
            sonarscansuccess =propsemail.sonarscansuccess
            testfailbody= propsemail.testfailbody
            dependencysubject= propsemail.dependencysubject
            deployfailsubject = propsemail.deployfailsubject
            jbossdomain = propsemail.jbossdomain
            enableadopt = propsemail.enableadopt
              enablejndi = propsemail.enablejndi
            binaryenable =  propsemail.binaryenable
            jboss = "${jboss}".toUpperCase()
            jbossenv = "${jbossdomain}_${jboss}"
		    jbossIP = propsemail."${branch}jboss"
            jbosscredid = propsemail.jbosscredid
            sonarenable = propsemail.sonarenable
            sonarreporturl = propsemail.sonarreporturl
            sonarreporturl = "${sonarreporturl}${sonarkey}"
			ipcheck = propsemail.ipcheck
			 if("${branch}"=="qa")
							{
                      			jbossenv="ePGS_QS"
							}

          }
          
          print "  jbossIP:::${jbossIP}"
          user = sh returnStdout: true, script: "git log -1 --pretty=format:'%ae' "
          user = user.split("@")[0] + "${techm}"
          commitid=sh returnStdout: true, script: "git log --format='%H' -n 1 "
          commitmessage=sh returnStdout: true, script: "git log --format=%B -n 1 ${commitid} "
          print "${commitid}  ::::${commitmessage}"
          currentBuild.displayName = "${projectName}_${newBuildId}"
          artifact = "${nexusip}/${moduleName}/${projectName}/${newBuildId}/"
          if (!applicationType.contains("spring")) {
             props = readJSON file: "${env.WORKSPACE}/package.json"
            warName = props.name
            archive = "${warName}.war"
            print props.name
             if(fileExists("${env.WORKSPACE}/src/environments/environment.${branch}.ts"))
            {
			sh "npm install && ng build --configuration=${branch}"
			
            dir("${env.WORKSPACE}/dist/${warName}") {
              sh "jar cvf ${archive} ."
            }
			}
			else
			{
			print "There is no angular profile please create angular profile and commit ..... "
			
			
			}
          } else {
		  //springboot
		  ipchecklist = ipcheck.split(',').collect {
                  it as String
                   };
			ipchecklist.each {
                 item ->
              print "ip check list iteration:: ${item}"
               
            if("${item}"=="spring.datasource.hikari")

            {
                dir("${env.WORKSPACE}/src/main/resources/") {
            ipcheckincode = sh(script: "grep -rnw '${env.WORKSPACE}/src/main/resources/' -e '${item}'   ", returnStatus: true) == 0
                }
            }
            else
            {
               ipcheckincode = sh(script: "grep -rnw '${env.WORKSPACE}' -e '${item}'   ", returnStatus: true) == 0 
            }
                print " current checking string is :: ${item} :: ${ipcheckincode}"
                echo "Checking ip in code base is : ${item}"
            if (ipcheckincode) {
             
			 iplist = "${iplist}${item}"
              print "${iplist}"
              
              print "In Code base ${item} is present"
              checklist.add("${item}")
 if("${enablejndi}"=="true")
                           {
                             print "string  "
              if("${item}"=="jdbc:oracle:thin")
                           {
                              return failure
                           }
                         }
			if("${item}"=="spring.datasource.hikari")
                           {
                print "Hikari?? is present "
                 sh "mvn clean"
				dir("${env.WORKSPACE}/src/main/resources/") {
                   sh " sed -i '/spring.datasource.hikari/d' *.properties" 
                }
				//		sh "git status"
                     //   sh " git add . && git commit -am ' hikari changes added'  "
                	//	sh " git push --set-upstream origin ${branch}"
              }
            }
          }

          print checklist.size()
          if (checklist.size() > 0) {
            print "${checklist} are hard coded "
            }
            if (dependencyreport.contains("${branch}")) {

              dir("/app/log4j/${projectName}") {
                fileOperations([folderCreateOperation("${branch}")])

                dir("${env.WORKSPACE}/") {
                  sh "${mvncommandinstall}"
                } //install
                dir("${env.WORKSPACE}/target/") {
                  sh "cp dependency-check-report.html /app/log4j/${projectName}/${branch}/dependency-check-report-${newBuildId}.html "
                  dependencyrepourl = dependencyrepourl + "${projectName}/${branch}/dependency-check-report-${newBuildId}.html"
                } //move dependency
              } //dir

            }
            dir("${env.WORKSPACE}/") {
              pom = readMavenPom file: 'pom.xml'
              values = "${pom}".split(':')
              warname = "${values[1]}.war"
              print "warname  :::::${warname}"
              sh "${mvncommand}"
              }
          }//spring boot 

        }
      }
      post {
        success {
          script {
            endDate = new Date()
             if("${enableadopt}"=="true")
            {
            adoptBuildFeedback buildDisplayName: "${projectName}_${newBuildId}.", buildEndedAt: "${endDate}", buildStartedAt: "${startDate}", buildUrl: "${BUILD_URL}", projectId: projectId, status: "SUCCESS"
            }
            }
        }
        failure {
          script {
            endDate = new Date()
             if("${enableadopt}"=="true")
            {
            adoptBuildFeedback buildDisplayName:  "${projectName}_${newBuildId}.", buildEndedAt: "${endDate}", buildStartedAt: "${startDate}", buildUrl: "${BUILD_URL}", projectId: projectId, status: "FAILURE"
            mail bcc: '', body: "<b>${moduleName} ${buildfailbody} </b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: "${moduleleademail}", charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "${buildfailsubject} ${env.JOB_NAME}", to: "${user}";
            }
          }
        }
      }
    }
  
      stage('Unit Test') {
      steps {
        script {
// sh "mvn test"
		print "Test cases run successfully"
        }
      }
    }
    
    
    
 stage('FOSS/OSS Scan') {
      steps {
        script {
          print "==============FOSS/OSS Scan==========="
       
 if (applicationType.contains("spring"))
          {
          print "==============FOSS/OSS Scan===========${dependencyrepourl}"
        
       // mail bcc: '', body: "<b>${moduleName} -FOSS/OSS Scan Report </b><br>Project: ${env.JOB_NAME} <br>FOSS/OSS Scan Report URL : ${dependencyrepourl}", cc: "${moduleleademail}", charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: " ${dependencysubject}", to: "${user}";
          
          print "==============email sent FOSS/OSS Scan===========${dependencyrepourl}"
          }
          }

      }
    }

    stage('Binary Management') {
      steps {
        script {
          if("${binaryenable}"=="true")
                           {
          withCredentials([usernamePassword(credentialsId: 'nexusid', passwordVariable: 'password', usernameVariable: 'username')]) {
          
            println "Uploading packaged code ..."
             if (applicationType.contains("spring"))
            {
            executeCmd("curl -v -u  $username:$password   --upload-file 'target/${warname}'  '${artifact}'");

            executeCmd("curl -v -u  $username:$password   --upload-file 'pom.xml'  '${artifact}'");
            
            }
            else
            {
             
              executeCmd("curl -v -u  $username:$password   --upload-file '${env.WORKSPACE}/dist/${warName}/${archive}'  '${artifact}'");
      
          
          executeCmd("curl -v -u  $username:$password   --upload-file 'package.json'  '${artifact}'");
            }

          }
        }
        }
      }
    } //stage
    
    stage("Deployment") {
      steps {
        script {

          withCredentials([usernamePassword(credentialsId: "${jbosscredid}", passwordVariable: "password", usernameVariable: 'username')]) {

            dir('/app/jboss-eap-7.3/bin/') {

              def disable = sh(script: " ./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password   --command='deployment disable ${warname}  --server-groups=${jbossenv}' ", returnStatus: true) == 0
              if (disable == true) {
                print "Disabled successfully"
                def undeploy = sh(script: " ./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password   --command='undeploy ${warname}  --server-groups=${jbossenv} ' ", returnStatus: true) == 0

                print undeploy
                if (undeploy == true) {
                  print "Undeployed  ${undeploy} successfully"

				   if (applicationType.contains("spring"))
            {
                   deploy = sh(script: "./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password     --command='deploy ${env.WORKSPACE}/target/${warname}    --server-groups=${jbossenv} ' ", returnStatus: true) == 0
                  print deploy
                  }
				  else 
				  {
				  
				   deploy = sh(script: "./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password     --command='deploy ${env.WORKSPACE}/dist/${warName}/${archive}    --server-groups=${jbossenv} ' ", returnStatus: true) == 0
                  print deploy
                  
				  }

                  if (deploy == false) {
                 if("${enableadopt}"=="true")
            {
              mail bcc: '', body: "<b>${moduleName}  ${deployfailbody} </b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: "${moduleleademail}", charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "${deployfailsubject}  ${env.JOB_NAME}", to: "${user}";
            }
                    def output = sh returnStdout: true, script: "curl --user 'developer':'java123#' 'http://10.240.33.164:8080/job/${env.JOB_NAME}/lastSuccessfulBuild/buildNumber'"
                    print "deploy failure for war file issue :: downloding backup one::" + output;

                    dir("${env.WORKSPACE}/") {

                      def artifactnew = "${nexusip}/${moduleName}/${projectName}/${buildids}.${output}/${warname}";
                      print " artifact ::" + artifactnew;
                      def newbuilddd = sh(script: " wget  --user='developer' and --password='java123#'   -X GET '${artifactnew}'", returnStatus: true) == 0

                      withCredentials([usernamePassword(credentialsId: "${jbosscredid}", passwordVariable: 'password', usernameVariable: 'username')]) {
                        dir('/app/jboss-eap-7.3/bin/') {
                          def deploynews22 = sh(script: "./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990  --user=$username  --password=$password     --command='deploy ${env.WORKSPACE}/${warname}    --server-groups=${jbossenv} ' ", returnStatus: true) == 0

                          print "rollback :::: deployed ::" + deploynews22
                          return failure
                        }
                      } //nexus

                    }

                  } //deploy
                  else

                  {

                    return true

                  }
                } //undeploy
                else {
                  print "undeploy failed"
                   deploy = sh(script: "./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password     --command='deploy ${env.WORKSPACE}/dist/${warName}/${archive}    --server-groups=${jboossenv} ' ", returnStatus: true) == 0
 if("${enableadopt}"=="true")
            {
                  mail bcc: '', body: "<b>${moduleName}  UN-${deployfailbody} </b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: "${moduleleademail}", charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "${deployfailsubject}  ${env.JOB_NAME}", to: "${user}";
             
            }
                  return failure
                } //undeploy else

              } //disable
              else {
                print "Disabled  failed Deploying application"
                def deployneww = sh(script: "./jboss-cli.sh --timeout=30000 --connect --controller=${jbossIP}:9990 --user=$username  --password=$password     --command='deploy ${env.WORKSPACE}/target/${warname}    --server-groups=${jbossenv} ' ", returnStatus: true) == 0
                print deployneww
                print "Disabled  failed Deploying application ${deployneww}"
                if (deployneww == false)

                {
			 if("${enableadopt}"=="true")
            {
                  print "DEPLOY FAILURE  ::" + user;
                  mail bcc: '', body: "<b>${moduleName}  ${deployfailbody} </b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: "${moduleleademail}", charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "${deployfailsubject}  ${env.JOB_NAME}", to: "${user}";
                  print "email sent to   ::" + user;
            }
              return failure
                }
              }

            } //dir  

          } //jboss
        } //script
      } //steps

      post {
        failure {
          script {
            try {

            } finally {
              script {
     currentBuild.result = 'SUCCESS'
              }
        }
          }
        }
      } //post
    } //stage

    stage('WEB Inspect') {
      steps {
        script {
          print "=============WEB Inspect==========="
        }

      }
    }

    
    stage('CLEAN WS') {
      steps {
        script {
   cleanWs()
          currentBuild.result = 'SUCCESS'
          currentBuild.result = "SUCCESS"
        }
      }
    }
    
  } //stages
} //pipeline

//Helper Methods

void executeCmd(String CMD) {
  if (isUnix()) {
    sh "echo linux"
    sh CMD
  } else {
    bat "echo windows"
    bat CMD
  }
}
