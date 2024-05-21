pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                git credentialsId: 'microservice-network-1', url: 'https://gitlab.com/longsoisuaxe1a/Management_BookStore_Microservice'
            }
        }
        stage('Matrix Build') {
            matrix {
                axes {
                    axis {
                        name 'SERVICE'
                        values 'BookService', 'CartService'
                    }
                }
                stages {
                    stage("Build ${SERVICE}") {
                        steps {
                            dir("${SERVICE}") {
                                sh 'mvn clean package'
                            }
                        }
                    }
                }
            }
        }
    }
}
