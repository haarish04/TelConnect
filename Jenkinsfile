pipeline {
    agent any

    environment {
        // Update the Maven path accordingly
        PATH = "C:/Users/e031975/Downloads/apache-maven-3.9.8-bin/apache-maven-3.9.8/bin;${env.PATH}"
        NODE_VERSION = '22.x'  // Update Node.js version to 22.x
    }

    stages {
        stage('Clone Repositories') {
            parallel {
                stage('Clone Backend') {
                    steps {
                        checkout([
                            $class: 'GitSCM',
                            branches: [[name: '*/main']],
                            userRemoteConfigs: [[url: 'https://github.com/haarish04/TelConnect']]
                        ])
                    }
                }
                stage('Clone Frontend') {
                    steps {
                        dir('frontend') {
                            checkout([
                                $class: 'GitSCM',
                                branches: [[name: '*/main']],
                                userRemoteConfigs: [[url: 'https://github.com/haarish04/telConnect-app.git']]
                            ])
                        }
                    }
                }
            }
        }

        stage('Build Backend with Maven') {
            steps {
                dir('TelConnect') {
                    // Maven build for the backend
                    bat 'mvn clean install'
                }
            }
        }

        stage('Run Backend Tests') {
            steps {
                dir('TelConnect') {
                    // Run backend test cases
                    bat 'mvn test'
                }
            }
        }

        stage('Install Frontend Dependencies') {
            steps {
                // Use Node.js v22.x environment for frontend
                nodejs(nodeJSInstallationName: 'NodeJS_22.x') {
                    dir('frontend') {
                        // Install frontend dependencies for the React-Vite app
                        bat 'npm install'
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                // Build the React-Vite frontend application using Node.js v22.x
                nodejs(nodeJSInstallationName: 'NodeJS_22.x') {
                    dir('frontend') {
                        bat 'npm run build'
                    }
                }
            }
        }

        stage('Run Backend and Frontend') {
            parallel {
                stage('Run Backend') {
                    steps {
                        // Run the Spring Boot backend
                        dir('TelConnect') {
                            bat 'start java -jar target/*.jar'
                        }
                    }
                }
                stage('Run Frontend') {
                    steps {
                        // Run the React app locally in dev mode using Node.js v22.x
                        nodejs(nodeJSInstallationName: 'NodeJS_22.x') {
                            dir('frontend') {
                                bat 'start npm run dev'
                            }
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs() // Clean workspace after execution
        }
        success {
            echo 'Build and run succeeded!'
        }
        failure {
            echo 'Build failed.'
        }
    }
}
