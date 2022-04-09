pipeline {
    agent any
    stages {
        stage('Send Slack Start') {
            agent any
            steps {
                slackSend (channel: '#jenkins', color: '#FFFF00', message: "STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
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
                sh 'docker build -t boated-be .'
                sh 'docker save -o boated-be.tar boated-be'
            }
        }
        stage('Transfer Image To Server') {
            steps {
                sh 'scp boated-be.tar ubuntu@15.164.89.188:/home/ubuntu/boated'
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