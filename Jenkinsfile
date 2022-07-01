pipeline {
    agent any
    stages {
        stage('Send Slack Start') {
            agent any
            steps {
                slackSend (channel: '#jenkins', color: '#FFFF00', message: "STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
            }
        }
        stage('Copy Credential') {
            steps {
                sh 'cp /var/jenkins_home/boated-be/application-deploy.properties src/main/resources/application-deploy.properties'
            }
        }
        stage('Gradlew grant Execute Permission') {
            steps {
                sh 'chmod +x ./gradlew'
            }
        }
        stage('Clean') {
            steps {
                sh './gradlew clean'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew test -Dorg.gradle.java.home=/usr/lib/jvm/java-17-openjdk-amd64'
            }
        }
        stage('Build') {
            steps {
                sh './gradlew bootJar -x test -Dorg.gradle.java.home=/usr/lib/jvm/java-17-openjdk-amd64'
            }
        }
        stage('Make Docker Image') {
            steps {
                sh 'docker build -t public.ecr.aws/g7j4u9e2/boated-be:$(git rev-parse --short HEAD) -t public.ecr.aws/g7j4u9e2/boated-be:latest .'
                sh 'docker push public.ecr.aws/g7j4u9e2/boated-be:$(git rev-parse --short HEAD)'
                sh 'docker push public.ecr.aws/g7j4u9e2/boated-be:latest'
            }
        }
        stage('Image') {
            steps {
                sh 'ssh ubuntu@15.164.89.188 "cd boated ; sh boated.sh"'
            }
        }
        stage('Delete Docker Image') {
            steps {
                sh 'docker rmi boated-be'
                sh 'rm boated-be.tar'
            }
        }
    }
    post {
        success {
            slackSend (channel: '#jenkins', color: '#00FF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
        failure {
            slackSend (channel: '#jenkins', color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
    }
}