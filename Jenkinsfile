pipeline {
    agent any
    tools {
        maven 'maven'
    }
    stages {
        stage('Build JAR File') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Grillitoxc/MueblesStgo']]])
                bat 'mvn clean install -DskipTests'
            }
        }
        stage('Tests') {
            steps {
                echo "Aquí va sonarqube"
            }
        }
        stage('Build Docker Image') {
            steps {
                bat 'docker build -t grillitoxc/mueblesstgo .'
            }
        }
        stage('Push docker image') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'dckrhubpassword', variable: 'dckpass')]) {
                        bat 'docker login -u grillitoxc -p ${dckpass}
                    }
                    bat 'docker push grillitoxc/mueblesstgo'
                }
            }
        }
    }
    post {
        always {
            bat 'docker logout'
        }
    }
}