pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                echo 'Test'
                sh 'echo $(pwd)'
            }
        }
        stage('Build') {
            steps {
                echo 'Build'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploy'
            }
        }
    }
}