pipeline {
    agent any
    stages {
        stage('Clean') {
            steps {
                sh './gradlew clean'
            }
        }
        stage('whoami') {
            steps {
                sh 'echo $(whoami)'
            }
        }
        stage('Test') {
            steps {
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