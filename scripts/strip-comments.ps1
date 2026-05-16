$paths = @()
$paths += Get-ChildItem -Path .\src -Include *.java,*.properties,*.ps1 -Recurse -File -ErrorAction SilentlyContinue
$paths += Get-ChildItem -Path .\src\main\docker -Filter 'Dockerfile*' -Recurse -File -ErrorAction SilentlyContinue
$paths += Get-ChildItem -Path .\scripts -Include *.ps1 -Recurse -File -ErrorAction SilentlyContinue
foreach ($f in $paths) {
    try {
        $text = Get-Content -Raw -Encoding UTF8 $f.FullName
        $text = [regex]::Replace($text,'(?s)/\*.*?\*/','')
        $text = [regex]::Replace($text,'(?m)^[\s]*//.*\r?\n','')
        $text = [regex]::Replace($text,'(?m)^[\s]*#.*\r?\n','')
        Set-Content -Path $f.FullName -Value $text -Encoding UTF8
    } catch {
        Write-Host "Failed: $($f.FullName) -> $_"
    }
}
Write-Host "Strip-comments completed"

