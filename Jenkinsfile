pipeline {
    agent any
    stages {
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
        stage('All Docker Image Delete') {
            steps {
                sh 'docker rmi $(docker images -q)'
            }
        }
        stage('Make Docker Image') {
            steps {
                sh 'docker build -t boated-be .'
            }
        }
    }
}