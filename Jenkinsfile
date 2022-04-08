pipeline {
    agent any
    stages {
        stage('Clean') {
            steps {
                sh './gradlew clean'
            }
        }
        stage('Test') {
            steps {
                sh 'echo $(whoami)'
                sh './gradlew test'
            }
        }
        stage('Build') {
            steps {
                sh './gradlew bootJar -x test'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploy'
            }
        }
    }
}